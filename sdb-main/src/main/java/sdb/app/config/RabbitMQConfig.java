package sdb.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String ORDER_PENDING_QUEUE = "order.pending.queue";
  public static final String ORDER_APPROVAL_QUEUE = "order.approval.queue";
  public static final String ORDER_EXCHANGE = "order.exchange";
  public static final String ROUTING_KEY_PENDING = "order.pending";
  public static final String ROUTING_KEY_APPROVAL = "order.approval";

  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange(ORDER_EXCHANGE);
  }

  @Bean
  public Queue orderPendingQueue() {
    return new Queue(ORDER_PENDING_QUEUE, true);
  }

  @Bean
  public Queue orderApprovalQueue() {
    return new Queue(ORDER_APPROVAL_QUEUE, true);
  }

  @Bean
  public Binding bindingPendingQueue(DirectExchange orderExchange, Queue orderPendingQueue) {
    return BindingBuilder.bind(orderPendingQueue).to(orderExchange).with(ROUTING_KEY_PENDING);
  }

  @Bean
  public Binding bindingApprovalQueue(DirectExchange orderExchange, Queue orderApprovalQueue) {
    return BindingBuilder.bind(orderApprovalQueue).to(orderExchange).with(ROUTING_KEY_APPROVAL);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
