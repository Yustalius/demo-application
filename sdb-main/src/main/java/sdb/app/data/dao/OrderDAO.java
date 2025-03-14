package sdb.app.data.dao;

import sdb.app.data.entity.order.OrderEntityOld;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {

  OrderEntityOld createOrder(OrderEntityOld purchase);

  Optional<List<OrderEntityOld>> getOrders();

  Optional<OrderEntityOld> getOrder(int purchaseId);

  Optional<List<OrderEntityOld>> getUserOrders(int userId);
}
