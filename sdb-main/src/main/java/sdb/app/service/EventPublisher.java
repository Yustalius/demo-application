package sdb.app.service;

import sdb.app.model.event.OrderCreatedEvent;

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