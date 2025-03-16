package sdb.app.service;

import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderDTO3;
import sdb.app.model.order.OrderStatus;

import java.util.List;

public interface OrderService3 {

  OrderDTO3 createOrder(OrderDTO3 order);

  OrderDTO3 updateStatus(int orderId, OrderStatus status);

  List<OrderDTO3> getOrders();

  OrderDTO3 getOrder(int orderId);

  List<OrderDTO3> getUserOrders(int userId);
}
