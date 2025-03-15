package sdb.app.utils.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.config.RabbitMQConfig;
import sdb.app.logging.Logger;
import sdb.app.model.order.OrderDTO;
import sdb.app.service.OrderService;

@Component
public class OrderApprovalListener {

  @Autowired
  private Logger logger;

  @Autowired
  private OrderService orderService;

/*  @RabbitListener(queues = RabbitMQConfig.ORDER_APPROVAL_QUEUE)
  public void handleOrderApproval(OrderDTO order) {
    logger.info("Order approval queue received message ", order);
    orderService.updateStatus(order.orderId(), order.status());
  }*/
}
