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

    // Общий ключ маршрутизации для всех событий заказа
    public static final String ORDER_EVENT_ROUTING_KEY = "order.event";

    // Константы для основных сообщений о заказах от главного сервиса
    public static final String CORE_EVENTS_EXCHANGE = "core-events-exchange";
    public static final String CORE_ORDER_EVENTS_QUEUE = "core-order-events-queue";

    // Константы для обработки сообщений от сервиса склада
    public static final String WAREHOUSE_EVENTS_EXCHANGE = "warehouse-events-exchange";

    /**
     * Создает Topic Exchange для событий заказа от главного сервиса.
     * Этот тип обмена отправляет сообщения в очереди на основе routing key,
     * что позволяет реализовать гибкую маршрутизацию.
     */
    @Bean
    public TopicExchange coreEventsExchange() {
        return new TopicExchange(CORE_EVENTS_EXCHANGE);
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
     * Создает единую очередь для обработки всех событий заказов в главном сервисе
     */
    @Bean
    public Queue coreOrderEventsQueue() {
        return QueueBuilder.durable(CORE_ORDER_EVENTS_QUEUE).build();
    }

    /**
     * Связывает очередь главного сервиса с warehouse exchange
     * для получения всех сообщений о заказах
     */
    @Bean
    public Binding coreOrderEventsBinding() {
        return BindingBuilder
            .bind(coreOrderEventsQueue())
            .to(warehouseEventsExchange())
            .with(ORDER_EVENT_ROUTING_KEY); // Используем один конкретный ключ маршрутизации
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
