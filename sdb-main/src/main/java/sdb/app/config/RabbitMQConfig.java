package sdb.app.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

public class RabbitMQConfig {

  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange("order_exchange");
  }

  @Bean
  public Queue orderProcessingQueue() {
    return QueueBuilder.durable("order_processing").build();
  }

  @Bean
  public Binding orderBinding() {
    return BindingBuilder
        .bind(orderProcessingQueue())
        .to(orderExchange())
        .with("order.process");
  }
}
