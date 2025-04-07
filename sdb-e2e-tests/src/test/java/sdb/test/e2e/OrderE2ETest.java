package sdb.test.e2e;

import org.junit.jupiter.api.Test;
import org.awaitility.Awaitility;
import org.awaitility.Durations;

import sdb.jupiter.annotation.Product;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.order.OrderStatus;
import sdb.model.product.ProductCoreDTO;
import sdb.model.user.UserDTO;
import sdb.service.OrderClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.model.order.OrderStatus.*;
import static org.awaitility.Awaitility.await;

public class OrderE2ETest {
  private final OrderClient orderClient = OrderClient.getInstance();

  @Test
  @User
  @Product(isAvailable = true, addToWarehouse = 100)
  public void orderApprovingTest(UserDTO user, ProductCoreDTO product) {
    OrderDTO order = orderClient.createOrder(new OrderDTO(
        user.id(),
        List.of(new OrderItemDTO(product.id(), product.price(), 1))));
    assertThat(order.orderId()).isNotNull();
    assertThat(order.status()).isEqualTo(PENDING);

    await()
        .atMost(Durations.TEN_SECONDS)
        .pollInterval(Durations.ONE_SECOND)
        .untilAsserted(() -> {
          OrderDTO orderInfo = orderClient.getOrder(order.orderId());
          assertThat(orderInfo.status()).isEqualTo(APPROVED);
        });
  }

  @Test
  @User
  @Product(addToWarehouse = 1)
  public void orderRejectingByStockTest(UserDTO user, ProductCoreDTO product) {
    OrderDTO order = orderClient.createOrder(new OrderDTO(
        user.id(),
        List.of(new OrderItemDTO(product.id(), product.price(), 2))));
    assertThat(order.orderId()).isNotNull();
    assertThat(order.status()).isEqualTo(PENDING);

    await()
        .atMost(Durations.TEN_SECONDS)
        .pollInterval(Durations.ONE_SECOND)
        .untilAsserted(() -> {
          OrderDTO orderInfo = orderClient.getOrder(order.orderId());
          assertThat(orderInfo.status()).isEqualTo(REJECTED);
          assertThat(orderInfo.rejectReasons()).anyMatch(reason ->
              reason.errorCode().equals("NOT_ENOUGH_STOCK") &&
                  reason.productId().equals(product.id()) &&
                  reason.availableStock().equals(1) &&
                  reason.requestedStock().equals(2)
          );
        });
  }
}
