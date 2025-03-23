package sdb.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.event.OrderEvent.ErrorMessage;
import sdb.warehouse.model.order.OrderItemDTO;
import utils.logging.Logger;

import java.util.Map;
import java.util.stream.Collectors;

import static sdb.warehouse.model.event.OrderEvent.OrderCode.ORDER_APPROVED;
import static sdb.warehouse.model.event.OrderEvent.OrderCode.ORDER_REJECTED;

/**
 * Реализация сервиса публикации событий с использованием RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher {

  // todo вынести в общую библиотеку
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger;

  // todo причины отказа, чтобы метод принимал причину и на ее основе составлял запрос
  public void publishOrderRejectedEvent(OrderEvent event, Map<OrderItemDTO, Pair<Integer, Integer>> productsWithQuantity) {
    try {
      event.setOrderCode(ORDER_REJECTED);
      event.setErrorMessages(
          productsWithQuantity.entrySet().stream()
              .map(entry -> new ErrorMessage(
                      "NOT_ENOUGH_STOCK",
                      entry.getKey().productId(),
                      entry.getValue().getFirst(),
                      entry.getValue().getSecond()
              ))
              .toList()
      );

      logger.info("Publishing order rejected event: ", event);
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.WAREHOUSE_EVENTS_EXCHANGE,
          RabbitMQConfig.ORDER_REJECTED_ROUTING_KEY,
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