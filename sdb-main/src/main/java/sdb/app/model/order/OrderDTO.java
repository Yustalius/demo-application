package sdb.app.model.order;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.SneakyThrows;
import sdb.app.data.entity.order.OrderEntity3;
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
    OrderProductDTO[] products,
    
    LocalDateTime timestamp,
    
    @NotNull(message = "Статус заказа не может быть null", groups = UpdateStatus.class)
    OrderStatus status
) {
  public static OrderDTO fromEntity(OrderEntity3 entity) {
    return new OrderDTO(
        entity.getOrderId(),
        entity.getUser().getId(),
        entity.getOrderItems().stream()
            .map(item -> new OrderProductDTO(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice()
            ))
            .toArray(OrderProductDTO[]::new),
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
