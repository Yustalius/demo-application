package sdb.warehouse.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.model.event.OrderCreatedEvent;
import utils.logging.Logger;

/**
 * Сервис для обработки событий заказов.
 */
@Service
public class OrderEventProcessor {
    @Autowired
    private Logger logger;

    /**
     * Обрабатывает событие создания заказа.
     * Получает сообщение из очереди order-created-queue и обрабатывает его.
     * 
     * @param event событие создания заказа
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_EVENTS_EXCHANGE)
    @Transactional
    public void processOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("Received order creation event: ", event);
        
        try {
            // Здесь будет логика обработки события
            // Например, резервирование товаров на складе

            logger.info("Order with ID %s successfully processed".formatted(event.getOrderId()));
        } catch (Exception e) {
            logger.error("Error processing order creation event: " +  e.getMessage(), e);
            // В реальном приложении здесь можно было бы отправить событие об ошибке
            // или перенаправить сообщение в очередь ошибок для последующей обработки
            throw e; // Перебрасываем исключение, чтобы сообщение не было подтверждено
        }
    }
} 