package sdb.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sdb.warehouse.config.RabbitMQConfig;
import sdb.warehouse.model.event.OrderEvent;
import utils.logging.Logger;

/**
 * Реализация сервиса публикации событий с использованием RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher {

    // todo вынести в общую библиотеку
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger;

    public void publishOrderCreatedEvent(OrderEvent event) {
        try {
            logger.info("Publishing order creation event: ", event);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EVENTS_EXCHANGE,
                    "",
                    event
            );
            logger.info("Order creation event with ID %s successfully published".formatted(event.getOrderId()));
        } catch (Exception e) {
            logger.error("Error publishing order creation event: %s".formatted(e.getMessage()), e);
        }
    }
} 