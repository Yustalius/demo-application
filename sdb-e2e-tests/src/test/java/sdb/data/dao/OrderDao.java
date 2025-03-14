package sdb.data.dao;


import sdb.data.entity.orders.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

  void createPurchase(OrderEntity... purchases);

  Optional<List<OrderEntity>> getPurchases();

  Optional<OrderEntity> getPurchase(int purchaseId);

  Optional<List<OrderEntity>> getUserPurchases(int userId);
}
