package sdb.app.utils.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.config.RabbitMQConfig;
import sdb.app.logging.Logger;
import sdb.app.model.order.OrderDTO;

@Component
public class OrderPendingListener {

  @Autowired
  private Logger logger;

/*  @RabbitListener(queues = RabbitMQConfig.ORDER_PENDING_QUEUE)
  public void handlePendingOrder(OrderDTO order) {
    // Передача заказа на склад
    logger.info("Order pending queue received message " + order);
  }*/
}