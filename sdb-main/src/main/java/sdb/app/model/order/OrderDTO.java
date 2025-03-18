package sdb.app.model.order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.SneakyThrows;
import sdb.app.data.entity.order.OrderEntity;
import sdb.app.model.validation.ValidationGroups.Create;
import sdb.app.model.validation.ValidationGroups.UpdateStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
//    @NotNull(message = "ID заказа не может быть null", groups = UpdateStatus.class)
//    @Min(value = 1, message = "ID продукта должен быть положительным числом", groups = UpdateStatus.class)
    Integer orderId,

    @NotNull(message = "ID пользователя не может быть null", groups = {Default.class, Create.class})
    Integer userId,

    @NotNull(message = "Список продуктов не может быть null", groups = {Default.class, Create.class})
    @NotEmpty(message = "Список продуктов не может быть пустым", groups = {Default.class, Create.class})
    @Valid
    List<OrderItemDTO> products,

    LocalDateTime timestamp,

    @NotNull(message = "Статус заказа не может быть null", groups = UpdateStatus.class)
    OrderStatus status
) {
  public static OrderDTO fromEntity(@Nonnull OrderEntity entity) {
      Integer userId = entity.getUser() != null ? entity.getUser().getId() : null;
      List<OrderItemDTO> orderItems = (entity.getOrderItems() != null) 
          ? entity.getOrderItems().stream()
              .map(item -> new OrderItemDTO(
                  (item.getProduct() != null) ? item.getProduct().getId() : null,
                  item.getQuantity(),
                  item.getPrice()
              ))
              .toList() 
          : Collections.emptyList();

      return new OrderDTO(
          entity.getOrderId(),
          userId,
          orderItems,
          entity.getCreatedAt(),
          entity.getStatus()
      );
  }

  public OrderDTO setStatus(OrderStatus status) {
    return new OrderDTO(
        this.orderId(),
        this.userId,
        this.products,
        this.timestamp,
        status
    );
  }

  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
