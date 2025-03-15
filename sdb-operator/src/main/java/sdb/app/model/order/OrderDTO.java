package sdb.app.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    Integer orderId,
    Integer userId,
    Integer productId,
    Integer price,
    Long timestamp,
    OrderStatus status
) {
  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
} 