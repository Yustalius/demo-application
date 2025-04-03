package sdb.service;


import sdb.model.order.OrderDTO;
import sdb.service.impl.OrderApiClient;

import java.util.List;

public interface OrderClient {

  static OrderClient getInstance() {
    return new OrderApiClient();
  }

  OrderDTO createOrder(OrderDTO order);

  List<OrderDTO> getOrders();

  OrderDTO getOrder(int id);

  List<OrderDTO> getUserOrders(int userId);
}
