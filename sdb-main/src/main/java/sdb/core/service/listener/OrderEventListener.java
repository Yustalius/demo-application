package sdb.core.service.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.config.RabbitMQConfig;
import sdb.core.model.event.OrderEvent;
import sdb.core.service.OrderService;
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

  /**
   * Обрабатывает событие отклонения заказа.
   * Получает сообщение из очереди core-order-rejected-queue и обрабатывает его.
   *
   * @param event событие отклонения заказа от сервиса склада
   */
  @RabbitListener(queues = RabbitMQConfig.CORE_ORDER_REJECTED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
  @Transactional
  public void processOrderRejectedEvent(OrderEvent event) {
    try {
      if (event == null || event.getOrderId() == null) {
        logger.error("Received invalid order rejection message: " + (event == null ? "null" : event.toString()));
        throw new AmqpRejectAndDontRequeueException("Invalid message format");
      }

      logger.info("Received order rejection event: " + event);

      switch (event.getOrderCode()) {
        case ORDER_REJECTED -> {
          List<OrderEvent.ErrorMessage> errorMessages = event.getErrorMessages();
          if (errorMessages != null && !errorMessages.isEmpty()) {
            JsonNode[] reasonsArray = errorMessages.stream()
                .map(errorMessage -> {
                  try {
                    return mapper.valueToTree(errorMessage);
                  } catch (Exception e) {
                    logger.error("Ошибка при преобразовании сообщения об ошибке в JsonNode: " + e.getMessage(), e);
                    return null;
                  }
                })
                .filter(Objects::nonNull)
                .toArray(JsonNode[]::new);

            orderService.rejectOrder(event.getOrderId(), reasonsArray);
          } else {
            orderService.rejectOrder(event.getOrderId());
          }
          logger.info("Order with ID " + event.getOrderId() + " has been rejected");
        }
        default -> {
          logger.error("Received event with unknown code: " + event.getOrderCode());
          throw new AmqpRejectAndDontRequeueException("Unknown event code: " + event.getOrderCode());
        }
      }
    } catch (AmqpRejectAndDontRequeueException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error while processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Unexpected error: " + e.getMessage(), e);
    }
  }
}