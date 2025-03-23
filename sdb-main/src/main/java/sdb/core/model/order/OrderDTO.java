package sdb.core.model.order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.SneakyThrows;
import sdb.core.data.entity.order.OrderEntity;
import sdb.core.model.validation.ValidationGroups.Create;
import sdb.core.model.validation.ValidationGroups.UpdateStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    @Schema(description = "ID заказа")
    Integer orderId,
    
    @Schema(description = "ID пользователя")
    @NotNull(message = "Поле 'userId' не может быть null", groups = {Default.class, Create.class})
    Integer userId,

    @Schema(description = "Список продуктов")
    @NotNull(message = "Поле 'items' не может быть null", groups = {Default.class, Create.class})
    @NotEmpty(message = "Поле 'items' не может быть пустым", groups = {Default.class, Create.class})
    @Valid
    List<OrderItemDTO> items,

    @Schema(description = "Временная метка")
    String timestamp,

    @Schema(description = "Статус заказа")
    @NotNull(message = "Поле 'status' не может быть null", groups = UpdateStatus.class)
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
          entity.getCreatedAt().toString(),
          entity.getStatus()
      );
  }

  public OrderDTO setStatus(OrderStatus status) {
    return new OrderDTO(
        this.orderId(),
        this.userId,
        this.items,
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
