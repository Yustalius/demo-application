package sdb.core.service.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.config.RabbitMQConfig;
import sdb.core.model.event.OrderEvent;
import sdb.core.model.order.OrderStatus;
import sdb.core.service.OrderService;
import sdb.core.service.impl.OrderEventProcessor;
import utils.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для обработки событий заказов, полученных от других сервисов.
 */
@Service
@RequiredArgsConstructor
public class OrderEventListener {

  private final Logger logger;
  private final ObjectMapper mapper;
  private final OrderService orderService;
  private final OrderEventProcessor eventProcessor;

  /**
   * Обрабатывает все события, связанные с заказами.
   * Получает сообщение из очереди core-order-events-queue и обрабатывает его.
   *
   * @param event событие заказа от сервиса склада
   */
  @RabbitListener(queues = RabbitMQConfig.CORE_ORDER_EVENTS_QUEUE, containerFactory = "rabbitListenerContainerFactory")
  @Transactional
  public void processOrderEvent(OrderEvent event) {
    logger.info("Received order event: " + event);

    if (event == null || event.getOrderId() == null) {
      logger.error("Received invalid order event message: " + (event == null ? "null" : event.toString()));
      throw new AmqpRejectAndDontRequeueException("Invalid message format");
    }

    switch (event.getOrderCode()) {
      case ORDER_APPROVED -> {
        logger.info("Order with ID " + event.getOrderId() + " has been approved");
        eventProcessor.processOrderApproval(event);
      }
      case ORDER_REJECTED -> {
        logger.info("Order " + event.getOrderId() + " has been rejected: " + event.getErrorMessages());
        eventProcessor.processOrderRejection(event);
      }
      default -> {
        logger.error("Received event with unknown code: " + event.getOrderCode());
        throw new AmqpRejectAndDontRequeueException("Unknown event code: " + event.getOrderCode());
      }
    }
  }
}