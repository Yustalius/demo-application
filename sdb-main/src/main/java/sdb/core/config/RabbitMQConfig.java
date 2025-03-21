package sdb.core.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ для обработки событий в событийной модели.
 * Используется FanoutExchange для доставки сообщений всем слушателям.
 */
@Configuration
public class RabbitMQConfig {

    // Имена для обмена и событий
    public static final String ORDER_EVENTS_EXCHANGE = "order-events-exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    /**
     * Создает Fanout Exchange для событий заказа.
     * Этот тип обмена отправляет сообщения во все связанные очереди,
     * что позволяет реализовать событийную модель с множеством потребителей.
     */
    @Bean
    public FanoutExchange orderEventsExchange() {
        return new FanoutExchange(ORDER_EVENTS_EXCHANGE);
    }

    /**
     * Конвертер сообщений JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Настраивает RabbitTemplate для отправки сообщений
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
