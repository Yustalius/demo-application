package sdb.warehouse.service;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.order.OrderItemDTO;
import sdb.warehouse.service.impl.OrderServiceImpl;
import utils.logging.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для обработки событий заказов.
 */
@Service
public class OrderEventListener {
  @Autowired
  private Logger logger;

  @Autowired
  private OrderServiceImpl orderService;

  /**
   * Обрабатывает событие создания заказа.
   * Получает сообщение из очереди warehouse-order-created-queue и обрабатывает его.
   *
   * @param event событие создания заказа
   */
  @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_ORDER_CREATED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
  @Transactional
  public void processOrderCreatedEvent(OrderEvent event) {
    try {
      validateEvent(event);

      logger.info("Received order event: ", event);
      Map<OrderItemDTO, String> orderItemErrors = new HashMap<>();

      try {
        Map<ProductEntity, Integer> productsWithQuantity = orderService.checkProductsInStock(event);
        for (OrderItemDTO item : event.getItems()) {
          if (item.productId() == null || item.quantity() == null || item.quantity() <= 0) {
            logger.error("Invalid order item: " + item);
            throw new AmqpRejectAndDontRequeueException("Invalid order item");
          }

          ProductEntity product = productsWithQuantity.keySet().stream()
              .filter(p -> p.getExternalProductId().equals(item.productId()))
              .findFirst()
              .orElseThrow(() -> {
                logger.error("Product with ID " + item.productId() + " not found");
                return new AmqpRejectAndDontRequeueException("Product not found");
              });

          if (item.quantity() > product.getStockQuantity()) {
            orderItemErrors.put(
                item,
                "Not enough stock available. In stock: %d, Required: %d"
                    .formatted(product.getStockQuantity(), item.quantity()));
          }
        }

        if (orderItemErrors.isEmpty()) {
          orderService.createOrders(productsWithQuantity, event.getOrderId());
          logger.info("Order with ID %s successfully created".formatted(event.getOrderId()));
        } else {
          logger.warn("Insufficient stock for products: " + orderItemErrors);
        }
      } catch (Exception e) {
        logger.error("Error processing message: " + e.getMessage(), e);
        throw new AmqpRejectAndDontRequeueException("Error processing message: " + e.getMessage(), e);
      }
    } catch (AmqpRejectAndDontRequeueException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Unexpected error: " + e.getMessage(), e);
    }
  }

  private void validateEvent(OrderEvent event) {
    if (event == null
        || event.getOrderId() == null
        || event.getItems() == null
        || event.getItems().isEmpty()
        || event.getOrderCode() == null) {
      logger.error("Received invalid message: " + (event == null ? "null" : event.toString()));
      throw new AmqpRejectAndDontRequeueException("Invalid message format");
    }
  }
}