package sdb.warehouse.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ для получения событий в событийной модели.
 */
@Configuration
public class RabbitMQConfig {

  public static final String ORDER_EVENTS_EXCHANGE = "order-events-exchange";
  public static final String WAREHOUSE_ORDER_CREATED_QUEUE = "warehouse-order-created-queue";

  /**
   * Создает Fanout Exchange для событий заказа
   */
  @Bean
  public FanoutExchange orderEventsExchange() {
    return new FanoutExchange(ORDER_EVENTS_EXCHANGE);
  }

  /**
   * Создает очередь для обработки событий создания заказа в сервисе склада
   */
  @Bean
  public Queue warehouseOrderCreatedQueue() {
    return QueueBuilder.durable(WAREHOUSE_ORDER_CREATED_QUEUE).build();
  }

  /**
   * Связывает очередь и fanout exchange
   * При использовании FanoutExchange ключ маршрутизации не используется
   */
  @Bean
  public Binding warehouseOrderCreatedBinding(Queue warehouseOrderCreatedQueue, FanoutExchange orderEventsExchange) {
    return BindingBuilder
        .bind(warehouseOrderCreatedQueue)
        .to(orderEventsExchange);
  }

  /**
   * Конвертер сообщений JSON
   */
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  /**
   * Настраивает RabbitTemplate для работы с сообщениями
   */
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }
}