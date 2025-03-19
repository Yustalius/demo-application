package sdb.core.model.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import sdb.core.model.order.OrderDTO;
import sdb.core.model.order.OrderItemDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Событие создания заказа.
 * Содержит информацию о созданном заказе, которая нужна для обработки в сервисе warehouse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private Integer orderId;
    private Integer userId;
    private List<OrderItemDTO> items;
    
    /**
     * Создает событие из объекта DTO заказа
     *
     * @param orderDTO DTO заказа
     * @return событие создания заказа
     */
    public static OrderCreatedEvent fromDTO(OrderDTO orderDTO) {
        return new OrderCreatedEvent(
            orderDTO.orderId(),
            orderDTO.userId(),
            orderDTO.products()
        );
    }
    
    @Override
    @SneakyThrows
    public String toString() {
        return objectMapper.writeValueAsString(this);
    }
} 