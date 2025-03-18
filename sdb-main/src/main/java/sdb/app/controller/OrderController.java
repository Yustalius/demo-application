package sdb.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sdb.app.model.order.OrderDTO;
import sdb.app.model.validation.ValidationGroups.Create;
import sdb.app.model.validation.ValidationGroups.UpdateStatus;
import sdb.app.service.OrderService;
import utils.logging.Logger;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
  private static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private Logger logger;

  @Autowired
  private OrderService orderService;

  @PostMapping("/add")
  public OrderDTO addOrder(@Validated(Create.class) @RequestBody OrderDTO order) throws JsonProcessingException {
    logger.info("Creating order: ", mapper.writeValueAsString(order));
    return orderService.createOrder(order);
  }

  @PostMapping("{id}/status")
  public OrderDTO updateStatus(
      @PathVariable int id,
      @Validated(UpdateStatus.class) @RequestBody OrderDTO order) {
    logger.info("Updating order productId = %s status to %s".formatted(id, order.status()));
    return orderService.updateStatus(id, order.status());
  }

  @GetMapping("/user/{id}")
  public List<OrderDTO> getUserOrders(@PathVariable int id) {
    logger.info("Get user orders productId = ", id);
    return orderService.getUserOrders(id);
  }

  @GetMapping("/{id}")
  public OrderDTO getOrder(@PathVariable int id) {
    logger.info("Get order productId = ", id);
    return orderService.getOrder(id);
  }

  @GetMapping
  public List<OrderDTO> getOrders() {
    logger.info("Get all orders");
    return orderService.getOrders();
  }
}
