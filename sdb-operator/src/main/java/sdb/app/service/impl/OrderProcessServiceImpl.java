package sdb.app.service.impl;

import org.springframework.stereotype.Service;
import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderStatus;
import sdb.app.service.OrderProcessService;

@Service
public class OrderProcessServiceImpl implements OrderProcessService {

  @Override
  public OrderDTO processOrder(OrderDTO order) {
    System.out.println("Processing order: " + order);
    
    // Здесь можно добавить бизнес-логику обработки заказа
    // Например, проверка наличия товара на складе, резервирование товара и т.д.
    
    // В данной реализации просто изменим статус заказа на APPROVED
    return new OrderDTO(
        order.orderId(),
        order.userId(),
        order.productId(),
        order.price(),
        order.timestamp(),
        OrderStatus.APPROVED
    );
  }
} 