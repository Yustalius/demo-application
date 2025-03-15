package sdb.app.service;

import sdb.app.model.order.OrderDTO;

public interface OrderProcessService {
  OrderDTO processOrder(OrderDTO order);
} 