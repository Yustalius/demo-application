package sdb.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.app.logging.Logger;
import sdb.app.model.order.OrderDTO3;
import sdb.app.model.validation.ValidationGroups.Create;
import sdb.app.model.validation.ValidationGroups.UpdateStatus;
import sdb.app.service.OrderService3;

@RestController
@RequestMapping("/test")
@Validated
public class TestController {
  private static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private Logger logger;

  @Autowired
  private OrderService3 orderService;

  @PostMapping("/add")
  public OrderDTO3 addOrder(@Validated(Create.class) @RequestBody OrderDTO3 order) throws JsonProcessingException {
    logger.info("Creating order: ", mapper.writeValueAsString(order));
    return orderService.createOrder(order);
  }

  @PostMapping("/update")
  public OrderDTO3 updateStatus(@Validated(UpdateStatus.class) @RequestBody OrderDTO3 order) {
    logger.info("Updating order id = %s status to %s".formatted(order.orderId(), order.status()));
    return orderService.updateStatus(order.orderId(), order.status());
  }
}
