package sdb.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.event.OrderEvent.ErrorMessage;
import sdb.warehouse.model.order.ErrorCode;
import sdb.warehouse.model.order.OrderItemDTO;
import utils.logging.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static sdb.warehouse.model.event.OrderEvent.OrderCode.ORDER_APPROVED;
import static sdb.warehouse.model.event.OrderEvent.OrderCode.ORDER_REJECTED;
import static sdb.warehouse.model.order.ErrorCode.ERROR;
import static sdb.warehouse.model.order.ErrorCode.NOT_ENOUGH_STOCK;

/**
 * Реализация сервиса публикации событий с использованием RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher {

  // todo вынести в общую библиотеку
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger;

  public void publishOrderApprovedEvent(OrderEvent event) {
    event.setOrderCode(ORDER_APPROVED);

    logger.info("Publishing order approved event: ", event);
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.WAREHOUSE_EVENTS_EXCHANGE,
        RabbitMQConfig.ORDER_EVENT_ROUTING_KEY,
        event
    );
  }

  public void publishOrderRejectedEvent(OrderEvent event) {
    publishOrderRejectedEvent(event, null, null, null);
  }

  public void publishOrderRejectedEvent(OrderEvent event, String description) {
    publishOrderRejectedEvent(event, null, description, null);
  }

  public void publishOrderRejectedEvent(OrderEvent event, ErrorCode errorCode) {
    publishOrderRejectedEvent(event, null, null, errorCode);
  }

  public void publishOrderRejectedEvent(OrderEvent event, Map<OrderItemDTO, Pair<Integer, Integer>> productsWithQuantity) {
    publishOrderRejectedEvent(event, productsWithQuantity, null, NOT_ENOUGH_STOCK);
  }

  public void publishOrderRejectedEvent(OrderEvent event,
                                        Map<OrderItemDTO, Pair<Integer, Integer>> productsWithQuantity,
                                        String description,
                                        ErrorCode errorCode) {
    try {
      event.setOrderCode(ORDER_REJECTED);
      if (productsWithQuantity != null) {
        event.setErrorMessages(
            productsWithQuantity.entrySet().stream()
                .map(entry -> new ErrorMessage(
                    errorCode != null ? errorCode : ERROR,
                    description,
                    entry.getKey().productId(),
                    entry.getValue().getFirst(),
                    entry.getValue().getSecond()
                ))
                .toList()
        );
      } else {
        event.setErrorMessages(Collections.singletonList(new ErrorMessage(ERROR, description)));
      }

      logger.info("Publishing order rejected event: ", event);
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.WAREHOUSE_EVENTS_EXCHANGE,
          RabbitMQConfig.ORDER_EVENT_ROUTING_KEY,
          event
      );
      logger.info("Successfully published order rejected event: ", event);
    } catch (Exception e) {
      logger.error(
          "Error publishing order rejected event for order %s: %s".formatted(event.getOrderId(), e.getMessage()), e);
      throw new RuntimeException(e);
    }
  }
} 