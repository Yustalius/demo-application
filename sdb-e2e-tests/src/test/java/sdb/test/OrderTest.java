package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Order;
import sdb.jupiter.annotation.User;
import sdb.model.product.OrderDTO;
import sdb.model.user.UserDTO;
import sdb.service.OrderClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

  private final OrderClient orderClient = OrderClient.getInstance();
  private static final int PRODUCT_ID = 293;

  @Test
  @User
  void addOrderTest(UserDTO user) {
    List<OrderDTO> orders = orderClient.createOrder(
        new OrderDTO(
            null,
            user.id(),
            PRODUCT_ID,
            200,
            null));

    assertThat(orders.getFirst().orderId()).isNotNull();
    assertThat(orders.getFirst().productId()).isEqualTo(PRODUCT_ID);
  }

  @Test
  @User(
      orders = @Order(
          productId = PRODUCT_ID,
          price = 300
      )
  )
  void getAllOrders() {
    assertThat(orderClient.getOrders()).isNotEmpty();
  }

  @Test
  @User(
      orders = @Order(
          productId = PRODUCT_ID,
          price = 200
      )
  )
  void getOrderTest(UserDTO user) {
    List<OrderDTO> purchases = orderClient.getUserOrders(user.id());
    OrderDTO randomOrder = purchases.stream()
        .findAny()
        .orElseThrow();

    OrderDTO purchase = orderClient.getOrder(randomOrder.orderId());
    assertThat(purchase.orderId()).isEqualTo(randomOrder.orderId());
  }

  @Test
  @User(
      orders = @Order(
          productId = PRODUCT_ID,
          price = 200
      )
  )
  void getUserOrdersTest(UserDTO user) {
    List<OrderDTO> userPurchases = orderClient.getUserOrders(user.id());

    assertThat(userPurchases).isNotEmpty();
  }
}
