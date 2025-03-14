package sdb.app.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.app.data.entity.order.OrderEntity;

public record OrderDTO(
    Integer orderId,
    Integer userId,
    @JsonProperty("productId")
    Integer productId,
    @JsonProperty("price")
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

  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
