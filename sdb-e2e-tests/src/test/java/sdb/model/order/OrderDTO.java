package sdb.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.data.entity.orders.OrderEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        entity.getUserId(),
        entity.getProductId(),
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
