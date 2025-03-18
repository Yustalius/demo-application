package sdb.warehouse.service;

import sdb.warehouse.model.order.OrderDTO;

public interface OrderProcessService {
  OrderDTO processOrder(OrderDTO order);
} 