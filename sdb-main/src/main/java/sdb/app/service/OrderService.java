package sdb.app.service;

import sdb.app.model.order.OrderDTO;

import java.util.List;

public interface OrderService {

  List<OrderDTO> createOrder(OrderDTO... purchases);

  List<OrderDTO> getOrders();

  OrderDTO getPurchase(int purchaseId);

  List<OrderDTO> getUserPurchases(int userId);
}
