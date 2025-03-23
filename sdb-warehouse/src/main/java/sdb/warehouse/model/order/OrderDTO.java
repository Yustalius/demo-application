package sdb.warehouse.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    Integer orderId,
    Integer userId,
    List<OrderItemDTO> items,
    String timestamp,
    OrderStatus status
) {
  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
} 