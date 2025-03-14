package sdb.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdb.app.model.purchase.OrderDTO;
import sdb.app.service.OrderService;
import sdb.app.logging.Logger;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
  private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  @Autowired
  private Logger logger;

  @Autowired
  private OrderService orderService;

  @PostMapping("/add")
  public List<OrderDTO> addOrder(@RequestBody OrderDTO... orders) throws JsonProcessingException {
    logger.info("Creating order: ", mapper.writeValueAsString(orders));
    return orderService.createOrder(orders);
  }

  @GetMapping("/user/{id}")
  public List<OrderDTO> getUserOrders(
      @PathVariable int id
  ) {
    logger.info("Get user orders id = ", id);
    return orderService.getUserPurchases(id);
  }

  @GetMapping("/{id}")
  public OrderDTO getOrder(
      @PathVariable int id
  ) {
    logger.info("Get order id = ", id);
    return orderService.getPurchase(id);
  }

  @GetMapping
  public List<OrderDTO> getOrders() {
    return orderService.getOrders();
  }
}
