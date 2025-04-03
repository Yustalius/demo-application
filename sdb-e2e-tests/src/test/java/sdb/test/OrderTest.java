package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Order;
import sdb.jupiter.annotation.OrderItem;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.user.UserDTO;
import sdb.service.OrderClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

  private final OrderClient orderClient = OrderClient.getInstance();
  private static final int PRODUCT_ID = 800;
  private static final Faker faker = new Faker();

  @Test
  @User
  void addOrderTest(UserDTO user) {
    OrderDTO order = orderClient.createOrder(
        new OrderDTO(
            null,
            user.id(),
            new OrderItemDTO[]{new OrderItemDTO(PRODUCT_ID, 200, 1)},
            null,
            null
        ));

    assertThat(order.orderId()).isNotNull();
    assertThat(order.items()).anyMatch(p -> p.productId() == PRODUCT_ID);
  }

  @Test
  @User(orders = @Order(
      orderItems = @OrderItem(
          productId = PRODUCT_ID,
          price = 200,
          quantity = 12
      )))
  void getAllOrders() {
    assertThat(orderClient.getOrders().size()).isGreaterThan(0);
  }

  @Test
  @User(orders = @Order(
      orderItems = @OrderItem(
          productId = PRODUCT_ID,
          price = 200,
          quantity = 1
      )))
  void getOrderTest(UserDTO user) {
    OrderDTO randomOrder = orderClient.getUserOrders(user.id()).stream()
        .findAny()
        .orElseThrow();

    OrderDTO purchase = orderClient.getOrder(randomOrder.orderId());
    assertThat(purchase.orderId()).isEqualTo(randomOrder.orderId());
  }

  @Test
  @User(orders = {
      @Order(orderItems = @OrderItem(
          productId = PRODUCT_ID,
          price = 200,
          quantity = 1
      )),
      @Order(orderItems = @OrderItem(
          productId = 720,
          price = 300,
          quantity = 2
      ))
  })
  void getUserOrdersTest(UserDTO user) {
    List<OrderDTO> orders = orderClient.getUserOrders(user.id());

    assertThat(orders.size()).isEqualTo(2);
  }
}
