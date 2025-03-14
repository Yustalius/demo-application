package sdb.service;


import sdb.model.order.OrderDTO;
import sdb.service.impl.OrderApiClient;

import java.util.List;

public interface OrderClient {

  static OrderClient getInstance() {
    return new OrderApiClient();
  }

  List<OrderDTO> createOrder(OrderDTO... purchases);

  List<OrderDTO> getOrders();

  OrderDTO getOrder(int purchaseId);

  List<OrderDTO> getUserOrders(int userId);
}
