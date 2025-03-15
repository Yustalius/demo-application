package sdb.app.utils.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sdb.app.config.RabbitMQConfig;
import sdb.app.logging.Logger;
import sdb.app.model.order.OrderDTO;

@Component
public class OrderPendingListener {

  @Autowired
  private Logger logger;

  @RabbitListener(queues = RabbitMQConfig.ORDER_PENDING_QUEUE, errorHandler = "rabbitExceptionHandler")
  public void handlePendingOrder(OrderDTO order) {
    // Передача заказа на склад
    logger.info("Order pending queue received message " + order);
  }
}