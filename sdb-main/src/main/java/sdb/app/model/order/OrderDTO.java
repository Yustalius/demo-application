package sdb.app.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.app.data.entity.order.OrderEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    Integer orderId,
    Integer userId,
    Integer productId,
    Integer price,
    Long timestamp,
    OrderStatus status
) {
  public static OrderDTO fromEntity(OrderEntity entity) {
    return new OrderDTO(
        entity.getPurchaseId(),
        entity.getUser().getId(),
        entity.getProduct() == null ? null : entity.getProduct().getId(),
        entity.getPrice(),
        entity.getTimestamp(),
        entity.getStatus()
    );
  }

  public OrderDTO setStatus(OrderStatus status) {
    return new OrderDTO(
        this.orderId(),
        this.userId,
        this.productId,
        this.price,
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
