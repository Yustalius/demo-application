package sdb.core.service;

import sdb.core.model.order.CreateOrderDTO;
import sdb.core.model.order.OrderDTO;
import sdb.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {

  OrderDTO createOrder(CreateOrderDTO order);

  OrderDTO updateStatus(int orderId, OrderStatus status);

  List<OrderDTO> getOrders();

  OrderDTO getOrder(int orderId);

  List<OrderDTO> getUserOrders(int userId);
}
