package sdb.app.utils.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.logging.Logger;

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