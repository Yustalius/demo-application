package sdb.jupiter.extension;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.config.Config;
import sdb.data.entity.purchases.PurchaseEntity;
import sdb.jupiter.annotation.Purchase;
import sdb.jupiter.annotation.User;
import sdb.model.product.PurchaseJson;
import sdb.model.user.UserDTO;
import sdb.service.impl.PurchaseDbClient;

import java.util.ArrayList;
import java.util.List;

import static sdb.data.Databases.dataSource;

public class PurchaseExtension implements BeforeEachCallback, ParameterResolver {
  private static final Config CFG = Config.getInstance();

  public static final Namespace NAMESPACE = Namespace.create(PurchaseExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.purchases())) {
            UserDTO user = context.getStore(UserExtension.NAMESPACE).get(
                context.getUniqueId(),
                UserDTO.class
            );

            final String username = user != null
                ? user.testData().username()
                : userAnno.username();

            final List<PurchaseJson> createdPurchases = new ArrayList<>();

            PurchaseDbClient purchaseClient = new PurchaseDbClient(dataSource(CFG.postgresUrl()));
            for (Purchase purchaseAnno : userAnno.purchases()) {
              PurchaseEntity purchase = new PurchaseEntity();
              purchase.setUserId(user.id());
              purchase.setProduct(purchaseAnno.product().toString());
              purchase.setPrice(purchaseAnno.price());

              purchaseClient.createPurchase(purchase);
              createdPurchases.add(PurchaseJson.fromEntity(purchase));
              user.testData().purchases().addAll(createdPurchases);
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(PurchaseJson[].class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public PurchaseJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (PurchaseJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .toArray(PurchaseJson[]::new);
  }
}
