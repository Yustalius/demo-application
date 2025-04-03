package sdb.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sdb.core.config.RabbitMQConfig;
import sdb.core.model.event.OrderEvent;
import sdb.core.model.order.OrderDTO;
import sdb.core.service.EventPublisher;
import utils.logging.Logger;

import static sdb.core.model.event.OrderEvent.OrderCode.ORDER_CREATED;
import static sdb.core.model.event.OrderEvent.OrderCode.ORDER_CANCELLED;

/**
 * Реализация сервиса публикации событий с использованием RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisher {

  // todo вынести в общую библиотеку
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger;

  @Override
  public void publishOrderCreatedEvent(OrderDTO orderDTO) {
    try {
      OrderEvent orderEvent = eventFromDTO(ORDER_CREATED, orderDTO);

      logger.info("Publishing order creation event: ", orderEvent);
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.CORE_EVENTS_EXCHANGE,
          RabbitMQConfig.ORDER_EVENT_ROUTING_KEY,
          orderEvent
      );
      logger.info("Successfully published order creation event: ", orderEvent);
    } catch (Exception e) {
      logger.error("Error publishing order creation event: %s".formatted(e.getMessage()), e);
    }
  }

  @Override
  public void publishOrderCancelledEvent(OrderDTO orderDTO) {
    try {
      OrderEvent orderEvent = eventFromDTO(ORDER_CANCELLED, orderDTO);

      rabbitTemplate.convertAndSend(
          RabbitMQConfig.CORE_EVENTS_EXCHANGE,
          RabbitMQConfig.ORDER_EVENT_ROUTING_KEY,
          orderEvent
      );
      logger.info("Successfully published order cancellation event: ", orderEvent);
    } catch (Exception e) {
      logger.error("Error publishing order cancellation event: %s".formatted(e.getMessage()), e);
    }
  }

  private static OrderEvent eventFromDTO(OrderEvent.OrderCode orderCreated, OrderDTO event) {
    OrderEvent orderEvent = new OrderEvent();
    orderEvent.setOrderCode(orderCreated);
    orderEvent.setOrderId(event.orderId());
    orderEvent.setItems(event.items());
    return orderEvent;
  }
}