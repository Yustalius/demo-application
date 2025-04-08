package sdb.service;


import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.order.OrderStatus;
import sdb.service.impl.OrderApiClient;

import java.util.List;

public interface OrderClient {

  static OrderClient getInstance() {
    return new OrderApiClient();
  }

  OrderDTO create(int userId, List<OrderItemDTO> items);

  OrderDTO updateStatus(int orderId, OrderStatus status);

  List<OrderDTO> get();

  OrderDTO getById(int id);

  List<OrderDTO> getByUserId(int userId);
}
