package sdb.app.service;

import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderStatus;

import java.util.List;

public interface OrderService {

  OrderDTO createOrder(OrderDTO order);

  OrderDTO updateStatus(int orderId, OrderStatus status);

  List<OrderDTO> getOrders();

  OrderDTO getOrder(int orderId);

  List<OrderDTO> getUserOrders(int userId);
}
