package sdb.app.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdb.app.config.RabbitMQConfig;
import sdb.app.logging.Logger;
import sdb.app.model.order.OrderDTO;

@RestController
@RequestMapping("/operator")
public class OperatorController {

  @Autowired
  private Logger logger;

  @Autowired
  private RabbitTemplate rabbit;

  @PostMapping("/order")
  public void addOrder(
      @RequestBody OrderDTO order) {
    logger.info("operator");
    rabbit.convertAndSend(
        RabbitMQConfig.ORDER_EXCHANGE,
        RabbitMQConfig.ROUTING_KEY_APPROVAL,
        order
    );
  }
}
