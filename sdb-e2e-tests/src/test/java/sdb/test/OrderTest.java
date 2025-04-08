package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Order;
import sdb.jupiter.annotation.OrderItem;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.order.OrderStatus;
import sdb.model.user.UserDTO;
import sdb.service.OrderClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.model.order.OrderStatus.CANCELLED;

public class OrderTest {

  private final OrderClient orderClient = OrderClient.getInstance();
  private static final int PRODUCT_ID = 151434706;

  @Test
  @User
  void addOrderTest(UserDTO user) {
    OrderDTO order = orderClient.create(
        user.id(),
        List.of(new OrderItemDTO(PRODUCT_ID, 200, 1))
    );

    assertThat(order.orderId()).isNotNull();
    assertThat(order.items()).anyMatch(p -> p.productId() == PRODUCT_ID);
  }

  @Test
  @User(orders = @Order(
      orderItems = @OrderItem(
          productId = PRODUCT_ID,
          price = 200,
          quantity = 1
      )))
  void updateOrderStatusTest(UserDTO user) {
    int orderId = user.testData().orders().getFirst().orderId();

    orderClient.updateStatus(orderId, CANCELLED);
    assertThat(orderClient.getById(orderId).status()).isEqualTo(CANCELLED);
  }

  @Test
  @User(orders = @Order(
      orderItems = @OrderItem(
          productId = PRODUCT_ID,
          price = 200,
          quantity = 1
      )))
  void getOrderTest(UserDTO user) {
    OrderDTO randomOrder = orderClient.getByUserId(user.id()).stream()
        .findAny()
        .orElseThrow();

    OrderDTO purchase = orderClient.getById(randomOrder.orderId());
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
          productId = PRODUCT_ID,
          price = 300,
          quantity = 2
      ))
  })
  void getUserOrdersTest(UserDTO user) {
    List<OrderDTO> orders = orderClient.getByUserId(user.id());

    assertThat(orders.size()).isEqualTo(2);
  }
}
