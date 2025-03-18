package sdb.warehouse.model.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import sdb.warehouse.model.order.OrderItemDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Событие создания заказа.
 * Содержит информацию о созданном заказе для обработки в сервисе warehouse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private Integer orderId;
    private Integer userId;
    private List<OrderItemDTO> items;
    
    @Override
    @SneakyThrows
    public String toString() {
        return objectMapper.writeValueAsString(this);
    }
} 