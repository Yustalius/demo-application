package sdb.warehouse.model.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import sdb.warehouse.model.order.ErrorCode;
import sdb.warehouse.model.order.OrderItemDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Событие создания заказа.
 * Содержит информацию о созданном заказе для обработки в сервисе warehouse.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEvent implements Serializable {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public record ErrorMessage(ErrorCode errorCode, String description, Integer productId, Integer availableStock, Integer requestedStock) {
    public ErrorMessage(ErrorCode errorCode, String description) {
      this(errorCode, description, null, null, null);
    }
  }

  public enum OrderCode {
    ORDER_CREATED,
    ORDER_REJECTED,
    ORDER_APPROVED,
    ORDER_CANCELLED,
  }

  private OrderCode orderCode;
  private List<ErrorMessage> errorMessages;
  private Integer orderId;
  private List<OrderItemDTO> items;

  @Override
  @SneakyThrows
  public String toString() {
    return objectMapper.writeValueAsString(this);
  }
} 