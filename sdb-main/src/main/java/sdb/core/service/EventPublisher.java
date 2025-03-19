package sdb.core.service;

import sdb.core.model.event.OrderCreatedEvent;

/**
 * Интерфейс для публикации событий в систему обмена сообщениями.
 */
public interface EventPublisher {
    
    /**
     * Публикует событие создания заказа.
     * 
     * @param event событие создания заказа
     */
    void publishOrderCreatedEvent(OrderCreatedEvent event);
} 