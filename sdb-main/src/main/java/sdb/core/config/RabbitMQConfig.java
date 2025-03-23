package sdb.core.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ.
 */
@Configuration
public class RabbitMQConfig {

    // Константы для основных сообщений о заказах от главного сервиса
    public static final String ORDER_EVENTS_EXCHANGE = "order-events-exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    // Константы для обработки сообщений от сервиса склада
    public static final String WAREHOUSE_EVENTS_EXCHANGE = "warehouse-events-exchange";
    public static final String ORDER_REJECTED_ROUTING_KEY = "order.rejected";
    public static final String CORE_ORDER_REJECTED_QUEUE = "core-order-rejected-queue";

    /**
     * Создает Topic Exchange для событий заказа от главного сервиса.
     * Этот тип обмена отправляет сообщения в очереди на основе routing key,
     * что позволяет реализовать гибкую маршрутизацию.
     */
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE);
    }

    /**
     * Создает Topic Exchange для событий от сервиса склада.
     * Через этот обмен сервис склада отправляет сообщения другим сервисам.
     */
    @Bean
    public TopicExchange warehouseEventsExchange() {
        return new TopicExchange(WAREHOUSE_EVENTS_EXCHANGE);
    }

    /**
     * Создает очередь для обработки отмененных заказов в главном сервисе
     */
    @Bean
    public Queue coreOrderRejectedQueue() {
        return QueueBuilder.durable(CORE_ORDER_REJECTED_QUEUE).build();
    }

    /**
     * Связывает очередь главного сервиса с warehouse exchange
     * для получения сообщений об отмененных заказах
     */
    @Bean
    public Binding coreOrderRejectedBinding() {
        return BindingBuilder
            .bind(coreOrderRejectedQueue())
            .to(warehouseEventsExchange())
            .with(ORDER_REJECTED_ROUTING_KEY);
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

    /**
     * Настройка фабрики слушателей RabbitMQ
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
