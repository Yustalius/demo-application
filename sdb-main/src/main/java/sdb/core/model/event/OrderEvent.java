package sdb.core.model.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import sdb.core.model.order.OrderItemDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Событие создания заказа.
 * Содержит информацию о созданном заказе, которая нужна для обработки в сервисе warehouse.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEvent implements Serializable {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorMessage(String errorCode, Integer productId, Integer availableStock, Integer requestedStock) {}

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