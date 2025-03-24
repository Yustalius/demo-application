package sdb.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.data.entity.orders.OrderEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    Integer orderId,
    Integer userId,
    OrderItemDTO[] items,
    String timestamp,
    OrderStatus status
) {
  public static OrderDTO fromEntity(OrderEntity entity) {
   return new OrderDTO(
       entity.getOrderId(),
       entity.getUserId(),
       entity.getOrderItems().stream()
           .map(item -> new OrderItemDTO(
               item.getProduct().getId(),
               item.getQuantity(),
               item.getPrice()
           ))
           .toArray(OrderItemDTO[]::new),
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
