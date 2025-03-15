package sdb.app.utils.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.config.RabbitMQConfig;
import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderStatus;
import sdb.app.service.OrderProcessService;
import sdb.app.utils.logging.Logger;

@Component
public class OrderPendingListener {
  @Autowired
  private Logger logger;

  @Autowired
  private OrderProcessService orderProcessService;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @RabbitListener(
      queues = RabbitMQConfig.ORDER_PENDING_QUEUE,
      containerFactory = "rabbitListenerContainerFactory"
  )
  public void handlePendingOrder(OrderDTO order) {
    logger.info("Начало обработки заказа: " + order);

    try {
      logger.info("Обработка заказа " + order.orderId() + " в процессе...");

      OrderDTO processedOrder = orderProcessService.processOrder(order);

      logger.info("Заказ " + order.orderId() + " успешно обработан.");

      // Отправка результата обработки обратно в очередь approval
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.ORDER_EXCHANGE,
          RabbitMQConfig.ROUTING_KEY_APPROVAL,
          processedOrder
      );

      logger.info("Заказ " + order.orderId() + " отправлен в очередь approval.");
    } catch (Exception e) {
      logger.info("Ошибка при обработке заказа " + order.orderId() + ": " + e.getMessage());
      // Здесь можно добавить логику обработки ошибок, например, отправку заказа в специальную очередь DLQ
    }
  }
} 