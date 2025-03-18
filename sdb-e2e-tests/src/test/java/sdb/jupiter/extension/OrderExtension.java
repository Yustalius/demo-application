package sdb.jupiter.extension;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.config.Config;
import sdb.data.entity.orders.OrderEntity;
import sdb.data.entity.orders.OrderItemEntity;
import sdb.data.entity.products.ProductEntity;
import sdb.data.entity.user.UsersEntity;
import sdb.jupiter.annotation.Order;
import sdb.jupiter.annotation.OrderItem;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderStatus;
import sdb.model.user.UserDTO;
import sdb.service.impl.OrderDbClient;
import sdb.service.impl.ProductApiClient;
import sdb.service.impl.UserApiClient;

import java.util.ArrayList;
import java.util.List;


import static java.util.Objects.requireNonNull;
import static sdb.data.Databases.dataSource;

/**
 * JUnit расширение для создания тестовых заказов.
 * Обрабатывает аннотацию {@link User} для создания заказов перед выполнением тестов.
 */
public class OrderExtension implements BeforeEachCallback, ParameterResolver {
  private static final Config CFG = Config.getInstance();
  public static final Namespace NAMESPACE = Namespace.create(OrderExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.orders())) {
            UserDTO user = context.getStore(UserExtension.NAMESPACE)
                .get(context.getUniqueId(), UserDTO.class);

            UsersEntity usersEntity = getUserEntity(user.id());

            OrderDbClient orderClient = new OrderDbClient(dataSource(CFG.postgresUrl()));
            ProductApiClient productApiClient = new ProductApiClient();

            List<OrderDTO> createdOrders = createOrdersForUser(
                userAnno.orders(),
                usersEntity,
                orderClient
            );

            user.testData().orders().addAll(createdOrders);
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

  /**
   * Получает сущность пользователя по ID
   *
   * @param userId ID пользователя
   * @return сущность пользователя
   */
  private UsersEntity getUserEntity(Integer userId) {
    UserApiClient userApiClient = new UserApiClient();
    return UsersEntity.fromJson(requireNonNull(userApiClient.getUser(userId)));
  }

  /**
   * Создает заказы для пользователя на основе аннотаций
   *
   * @param orderAnnotations массив аннотаций заказов
   * @param usersEntity сущность пользователя
   * @param orderClient клиент для работы с заказами
   * @return список созданных заказов в виде DTO
   */
  private List<OrderDTO> createOrdersForUser(
      Order[] orderAnnotations,
      UsersEntity usersEntity,
      OrderDbClient orderClient
  ) {
    List<OrderDTO> createdOrders = new ArrayList<>();
    ProductApiClient productApiClient = new ProductApiClient();

    for (Order orderAnno : orderAnnotations) {
      List<OrderItemEntity> orderItems = createOrderItems(orderAnno.orderItems(), productApiClient);
      OrderEntity orderEntity = createOrderEntity(usersEntity, orderItems);

      Integer createdOrderId = orderClient.createOrderWithOrderItems(
          orderEntity,
          orderItems.toArray(new OrderItemEntity[0])
      );

      OrderEntity savedOrder = requireNonNull(orderClient.getOrder(createdOrderId));
      createdOrders.add(OrderDTO.fromEntity(savedOrder));
    }

    return createdOrders;
  }

  /**
   * Создает элементы заказа на основе аннотаций
   *
   * @param orderItemAnnotations массив аннотаций элементов заказа
   * @param productApiClient клиент для работы с товарами
   * @return список созданных элементов заказа
   */
  private List<OrderItemEntity> createOrderItems(
      OrderItem[] orderItemAnnotations,
      ProductApiClient productApiClient
  ) {
    List<OrderItemEntity> orderItems = new ArrayList<>();

    for (OrderItem orderItemAnno : orderItemAnnotations) {
      ProductEntity product = ProductEntity.fromDTO(
          requireNonNull(productApiClient.getProductById(orderItemAnno.productId()))
      );

      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setProduct(product);
      orderItemEntity.setQuantity(orderItemAnno.quantity());
      orderItemEntity.setPrice(orderItemAnno.price());

      orderItems.add(orderItemEntity);
    }

    return orderItems;
  }

  /**
   * Создает сущность заказа
   *
   * @param usersEntity сущность пользователя
   * @param orderItems список элементов заказа
   * @return сущность заказа
   */
  private OrderEntity createOrderEntity(UsersEntity usersEntity, List<OrderItemEntity> orderItems) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUser(usersEntity);
    orderEntity.setStatus(OrderStatus.PENDING);
    orderEntity.setOrderItems(orderItems);

    return orderEntity;
  }
}