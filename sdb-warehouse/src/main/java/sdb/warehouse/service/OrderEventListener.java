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
    } catch (AmqpRejectAndDontRequeueException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error processing message: " + e.getMessage(), e);
      throw new AmqpRejectAndDontRequeueException("Unexpected error: " + e.getMessage(), e);
    }
  }
}