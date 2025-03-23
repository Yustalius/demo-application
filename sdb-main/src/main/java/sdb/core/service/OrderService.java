package sdb.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import sdb.core.model.event.OrderEvent;
import sdb.core.model.order.CreateOrderDTO;
import sdb.core.model.order.OrderDTO;
import sdb.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {

  OrderDTO createOrder(CreateOrderDTO order);

  OrderDTO updateStatus(int orderId, OrderStatus status);

  OrderDTO rejectOrder(int orderId, JsonNode... reasons);

  List<OrderDTO> getOrders();

  OrderDTO getOrder(int orderId);

  List<OrderDTO> getUserOrders(int userId);
}
