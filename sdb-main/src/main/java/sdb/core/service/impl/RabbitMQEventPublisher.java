package sdb.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sdb.core.config.RabbitMQConfig;
import sdb.core.model.event.OrderCreatedEvent;
import sdb.core.service.EventPublisher;
import utils.logging.Logger;

/**
 * Реализация сервиса публикации событий с использованием RabbitMQ.
 */
@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Logger logger;

    @Override
    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            logger.info("Publishing order creation event: ", event);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                    event
            );
            logger.info("Order creation event with ID %s successfully published".formatted(event.getOrderId()));
        } catch (Exception e) {
            logger.error("Error publishing order creation event: %s".formatted(e.getMessage()), e);
        }
    }
} 