package sdb.warehouse.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ для получения событий.
 */
@Configuration
public class RabbitMQConfig {

  // Имена для обмена и очереди
  public static final String ORDER_EXCHANGE = "order-exchange";
  public static final String ORDER_CREATED_QUEUE = "order-created-queue";
  public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

  /**
   * Создает обмен для событий заказа
   */
  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange(ORDER_EXCHANGE);
  }

  /**
   * Создает очередь для событий создания заказа
   */
  @Bean
  public Queue orderCreatedQueue() {
    return QueueBuilder.durable(ORDER_CREATED_QUEUE).build();
  }

  /**
   * Связывает очередь и обмен с ключом маршрутизации
   */
  @Bean
  public Binding orderCreatedBinding(Queue orderCreatedQueue, DirectExchange orderExchange) {
    return BindingBuilder
        .bind(orderCreatedQueue)
        .to(orderExchange)
        .with(ORDER_CREATED_ROUTING_KEY);
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