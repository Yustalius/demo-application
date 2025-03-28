package sdb.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.order.OrderItemDTO;
import sdb.warehouse.service.impl.OrderServiceImpl;
import utils.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProcessor {

  private final Logger logger;
  private final RabbitMQEventPublisher eventPublisher;
  private final OrderServiceImpl orderService;

  public void processOrderEvent(OrderEvent event) {
    validateEvent(event);

    switch (event.getOrderCode()) {
      case ORDER_CREATED:
        processOrderCreatedEvent(event);
        break;
      default:
        // todo отправлять отмену заказа
        throw new IllegalArgumentException("Unknown order code: " + event.getOrderCode());
    }
  }

  @Transactional
  private void processOrderCreatedEvent(OrderEvent event) {
    try {
      Map<OrderItemDTO, ProductEntity> orderItemProductEntityMap = orderService.findProductsInDatabaseByDto(event);
      // мапа для хранения ошибок по недостатку товаров, ключ - товар, значение - пара (текущий остаток, запрошенное количество)
      Map<OrderItemDTO, Pair<Integer, Integer>> orderStockErrors = new HashMap<>();

      for (OrderItemDTO item : event.getItems()) {
        ProductEntity productEntity = orderItemProductEntityMap.get(item);

        if (item.quantity() > productEntity.getStockQuantity()) {
          orderStockErrors.put(item, Pair.of(productEntity.getStockQuantity(), item.quantity()));
        }
      }

      if (orderStockErrors.isEmpty()) {
        orderItemProductEntityMap.entrySet().forEach(entry -> {
          orderService.createOrders(Map.of(entry.getValue(), entry.getKey().quantity()), event.getOrderId());
        });
        eventPublisher.publishOrderApprovedEvent(event);
        logger.info("Order with ID %s successfully created".formatted(event.getOrderId()));
      } else {
        logger.error("Insufficient stock for products: " + orderStockErrors);
        eventPublisher.publishOrderRejectedEvent(event, orderStockErrors);
      }
    } catch (Exception e) {
      logger.error("Error processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Error processing message: " + e.getMessage(), e);
    }
  }

  private void validateEvent(OrderEvent event) {
    if (isInvalidEvent(event)) {
      logger.error("Received invalid message: " + (event == null ? "null" : event.toString()));
      // todo отправлять отмену заказа
      throw new AmqpRejectAndDontRequeueException("Invalid message format");
    }

    for (OrderItemDTO item : event.getItems()) {
      if (isInvalidOrderItem(item)) {
        logger.error("Invalid order item: " + item);
        // todo отправлять отмену заказа
        throw new AmqpRejectAndDontRequeueException("Invalid order item");
      }
    }
  }

  private boolean isInvalidEvent(OrderEvent event) {
    return event == null || event.getOrderId() == null || event.getItems() == null || event.getItems().isEmpty() || event.getOrderCode() == null;
  }

  private boolean isInvalidOrderItem(OrderItemDTO item) {
    return item.productId() == null || item.quantity() == null || item.quantity() <= 0;
  }
}
