package sdb.core.service;

import sdb.core.model.event.OrderEvent;
import sdb.core.model.order.OrderDTO;

/**
 * Интерфейс для публикации событий в систему обмена сообщениями.
 */
public interface EventPublisher {
    
    /**
     * Публикует событие создания заказа.
     * 
     * @param event событие создания заказа
     */
    void publishOrderCreatedEvent(OrderDTO event);

    /**
     * Публикует событие отмены заказа.
     * 
     * @param event событие отмены заказа
     */
    void publishOrderCancelledEvent(OrderDTO event);
} 