package sdb.warehouse.service;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.model.event.OrderEvent;
import utils.logging.Logger;

/**
 * Сервис для обработки событий заказов.
 */
@Service
public class OrderEventListener {
  @Autowired
  private Logger logger;

  @Autowired
  private OrderEventProcessor processor;

  /**
   * Обрабатывает событие заказа.
   * Получает сообщение из очереди warehouse-order-event-queue и обрабатывает его.
   *
   * @param event событие заказа
   */
  @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_ORDER_EVENT_QUEUE, containerFactory = "rabbitListenerContainerFactory")
  @Transactional
  public void processOrderEvent(OrderEvent event) {
    try {
      logger.info("Received order event: ", event);
      processor.processOrderEvent(event);
    } catch (Exception e) {
      logger.error("Unexpected error processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Unexpected error: " + e.getMessage(), e);
    }
  }
}