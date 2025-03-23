package sdb.warehouse.service;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.order.OrderItemDTO;
import sdb.warehouse.service.impl.OrderServiceImpl;
import utils.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static sdb.warehouse.model.event.OrderEvent.OrderCode.ORDER_CREATED;

@Service
public class OrderEventProcessor {

  @Autowired
  private Logger logger;
  @Autowired
  private OrderServiceImpl orderService;

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

  private void processOrderCreatedEvent(OrderEvent event) {
    try {
      Map<ProductEntity, Integer> productsWithQuantity = orderService.checkProductsInStock(event);
      Map<OrderItemDTO, String> orderItemErrors = new HashMap<>();

      for (OrderItemDTO item : event.getItems()) {
        if (item.productId() == null || item.quantity() == null || item.quantity() <= 0) {
          logger.error("Invalid order item: " + item);
          // todo отправлять отмену заказа
          throw new AmqpRejectAndDontRequeueException("Invalid order item");
        }

        ProductEntity product = productsWithQuantity.keySet().stream()
            .filter(p -> p.getExternalProductId().equals(item.productId()))
            .findFirst()
            .orElseThrow(() -> {
              logger.error("Product with ID " + item.productId() + " not found");
              // todo отправлять отмену заказа
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
        // todo отправлять отмену заказа
        logger.error("Insufficient stock for products: " + orderItemErrors);
      }
    } catch (Exception e) {
      logger.error("Error processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Error processing message: " + e.getMessage(), e);
    }
  }

  private void validateEvent(OrderEvent event) {
    if (event == null
        || event.getOrderId() == null
        || event.getItems() == null
        || event.getItems().isEmpty()
        || event.getOrderCode() == null) {
      logger.error("Received invalid message: " + (event == null ? "null" : event.toString()));
      // todo отправлять отмену заказа
      throw new AmqpRejectAndDontRequeueException("Invalid message format");
    }
  }
}
