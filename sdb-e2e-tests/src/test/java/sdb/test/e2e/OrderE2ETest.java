package sdb.test.e2e;

import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.junit.jupiter.api.Test;
import org.awaitility.Durations;

import sdb.jupiter.annotation.Product;
import sdb.jupiter.annotation.User;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.product.ProductCoreDTO;
import sdb.model.user.UserDTO;
import sdb.service.OrderClient;
import sdb.service.WhProductClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.model.order.OrderStatus.*;
import static org.awaitility.Awaitility.await;
import static sdb.utils.AwaitUtils.waitFor;

public class OrderE2ETest {
  private final OrderClient orderClient = OrderClient.getInstance();
  private final WhProductClient whProductClient = WhProductClient.getInstance();

  @Test
  @User
  @Product(isAvailable = true, addToWarehouse = 100)
  public void orderApprovingTest(UserDTO user, ProductCoreDTO product) {
    OrderDTO order = createOrder(user, product, 2);
    assertThat(order.orderId()).isNotNull();
    assertThat(order.status()).isEqualTo(PENDING);

    waitFor(() -> {
      OrderDTO orderInfo = orderClient.getById(order.orderId());
      assertThat(orderInfo.status()).isEqualTo(APPROVED);
    });
  }

  @Test
  @User
  @Product(addToWarehouse = 1)
  public void orderRejectingByStockTest(UserDTO user, ProductCoreDTO product) {
    OrderDTO order = createOrder(user, product, 2);
    assertThat(order.orderId()).isNotNull();
    assertThat(order.status()).isEqualTo(PENDING);

    waitFor(() -> {
      OrderDTO orderInfo = orderClient.getById(order.orderId());
      assertThat(orderInfo.status()).isEqualTo(REJECTED);
      assertThat(orderInfo.rejectReasons()).anyMatch(reason ->
          reason.errorCode().equals("NOT_ENOUGH_STOCK") &&
              reason.productId().equals(product.id()) &&
              reason.availableStock().equals(1) &&
              reason.requestedStock().equals(2)
      );
    });
  }

  @Test
  @User
  @Product(addToWarehouse = 5)
  void cancelledProductsReturnsToStockQuantityTest(UserDTO user, ProductCoreDTO product) {
    OrderDTO order = createOrder(user, product, 5);
    waitFor(() -> assertThat(whProductClient.getByExternalId(product.id()).stockQuantity()).isZero());

    orderClient.updateStatus(order.orderId(), CANCELLED);
    waitFor(() -> assertThat(whProductClient.getByExternalId(product.id()).stockQuantity()).isEqualTo(5));
  }

  private OrderDTO createOrder(UserDTO user, ProductCoreDTO product, int quantity) {
    return orderClient.create(
        user.id(),
        List.of(new OrderItemDTO(product.id(), product.price(), quantity))
    );
  }
}
