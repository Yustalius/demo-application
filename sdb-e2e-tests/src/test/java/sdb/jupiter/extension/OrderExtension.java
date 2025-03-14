package sdb.jupiter.extension;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.config.Config;
import sdb.data.entity.orders.OrderEntity;
import sdb.jupiter.annotation.Order;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.user.UserDTO;
import sdb.service.impl.OrderDbClient;

import java.util.ArrayList;
import java.util.List;

import static sdb.data.Databases.dataSource;

public class OrderExtension implements BeforeEachCallback, ParameterResolver {
  private static final Config CFG = Config.getInstance();

  public static final Namespace NAMESPACE = Namespace.create(OrderExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.orders())) {
            UserDTO user = context.getStore(UserExtension.NAMESPACE).get(
                context.getUniqueId(),
                UserDTO.class
            );

            final String username = user != null
                ? user.testData().username()
                : userAnno.username();

            final List<OrderDTO> createdPurchases = new ArrayList<>();

            OrderDbClient orderClient = new OrderDbClient(dataSource(CFG.postgresUrl()));
            for (Order purchaseAnno : userAnno.orders()) {
              OrderEntity order = new OrderEntity();
              order.setUserId(user.id());
              order.setProductId(purchaseAnno.productId());
              order.setPrice(purchaseAnno.price());

              orderClient.createPurchase(order);
              createdPurchases.add(OrderDTO.fromEntity(order));
              user.testData().orders().addAll(createdPurchases);
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(OrderDTO[].class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public OrderDTO[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (OrderDTO[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .toArray(OrderDTO[]::new);
  }
}
