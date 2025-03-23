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
  public void publishOrderCreatedEvent(OrderDTO event) {
    try {
      OrderEvent orderEvent = new OrderEvent();
      orderEvent.setOrderCode(ORDER_CREATED);
      orderEvent.setOrderId(event.orderId());
      orderEvent.setItems(event.items());

      logger.info("Publishing order creation event: ", event);
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.ORDER_EVENTS_EXCHANGE,
          "",
          orderEvent
      );
      logger.info("Successfully published order creation event: ", orderEvent);
    } catch (Exception e) {
      logger.error("Error publishing order creation event: %s".formatted(e.getMessage()), e);
    }
  }
} 