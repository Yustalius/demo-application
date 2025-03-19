package sdb.core.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sdb.core.model.order.CreateOrderDTO;
import sdb.core.model.order.OrderDTO;
import sdb.core.model.order.StatusDTO;
import sdb.core.model.validation.ValidationGroups.Create;
import sdb.core.model.validation.ValidationGroups.UpdateStatus;
import sdb.core.service.OrderService;
import utils.logging.Logger;

@RestController
@RequestMapping("/order")
@Validated
@Tag(name = "Заказы", description = "Операции с заказами")
public class OrderController {
  private static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private Logger logger;

  @Autowired
  private OrderService orderService;

  @Operation(summary = "Добавление заказа", description = "Создает новый заказ на основе предоставленных данных.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Заказ успешно добавлен",
          content = @Content(schema = @Schema(implementation = OrderDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "UserNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("/add")
  public OrderDTO addOrder(@RequestBody CreateOrderDTO order) throws JsonProcessingException {
    logger.info("Creating order: ", mapper.writeValueAsString(order));
    return orderService.createOrder(order);
  }

  @Operation(summary = "Обновление статуса заказа", description = "Обновляет статус существующего заказа по его ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Статус заказа успешно обновлен",
          content = @Content(schema = @Schema(implementation = OrderDTO.class))),
      @ApiResponse(responseCode = "400", ref = "StatusTransitionErrorResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "OrderNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("{id}/status")
  public OrderDTO updateStatus(
      @PathVariable int id,
      @RequestBody StatusDTO order) {
    logger.info("Updating order productId = %s status to %s".formatted(id, order.status()));
    return orderService.updateStatus(id, order.status());
  }

  @Operation(summary = "Получение заказов пользователя",
      description = "Получает список заказов для указанного пользователя по его ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Заказы пользователя успешно получены",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "UserNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping("/user/{userId}")
  public List<OrderDTO> getUserOrders(@PathVariable("userId") int userId) {
    logger.info("Get user orders userId = ", userId);
    return orderService.getUserOrders(userId);
  }

  @Operation(summary = "Получение заказа по ID", description = "Возвращает заказ по указанному ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Заказ успешно получен",
          content = @Content(schema = @Schema(implementation = OrderDTO.class))),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "OrderNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping("/{id}")
  public OrderDTO getOrder(@PathVariable int id) {
    logger.info("Get order productId = ", id);
    return orderService.getOrder(id);
  }

  @Operation(summary = "Получение всех заказов", description = "Возвращает список всех заказов.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Заказы успешно получены",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping
  public List<OrderDTO> getOrders() {
    logger.info("Get all orders");
    return orderService.getOrders();
  }
}
