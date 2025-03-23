package sdb.warehouse.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;
import utils.logging.Logger;

/**
 * Конфигурация RabbitMQ для получения событий в событийной модели.
 */
@Configuration
public class RabbitMQConfig {

  public static final String ORDER_EVENTS_EXCHANGE = "order-events-exchange";
  public static final String WAREHOUSE_ORDER_CREATED_QUEUE = "warehouse-order-created-queue";
  public static final String DLX_EXCHANGE = "dlx-exchange";
  public static final String DLQ_QUEUE = "dlq";

  /**
   * Создает Dead Letter Exchange для перенаправления неудачных сообщений
   */
  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(DLX_EXCHANGE);
  }

  /**
   * Создает Dead Letter Queue для хранения неудачных сообщений
   */
  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(DLQ_QUEUE).build();
  }

  /**
   * Связывает DLQ и DLX
   */
  @Bean
  public Binding deadLetterBinding() {
    return BindingBuilder
        .bind(deadLetterQueue())
        .to(deadLetterExchange())
        .with("deadLetter");
  }

  /**
   * Создает Fanout Exchange для событий заказа
   */
  @Bean
  public FanoutExchange orderEventsExchange() {
    return new FanoutExchange(ORDER_EVENTS_EXCHANGE);
  }

  /**
   * Создает очередь для обработки событий создания заказа в сервисе склада
   * с настройкой перенаправления в DLQ при ошибках
   */
  @Bean
  public Queue warehouseOrderCreatedQueue() {
    return QueueBuilder.durable(WAREHOUSE_ORDER_CREATED_QUEUE)
        .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
        .withArgument("x-dead-letter-routing-key", "deadLetter")
        .withArgument("x-message-ttl", 30000) // 30 секунд TTL
        .build();
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
  
  /**
   * Кастомный обработчик ошибок для логирования
   */
  @Bean
  public ErrorHandler errorHandler(Logger logger) {
    return new ConditionalRejectingErrorHandler(new CustomErrorHandler(logger));
  }
  
  /**
   * Настройка фабрики слушателей RabbitMQ с нашим обработчиком ошибок
   */
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
          ConnectionFactory connectionFactory, 
          ErrorHandler errorHandler) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setErrorHandler(errorHandler);
    factory.setMessageConverter(jsonMessageConverter());
    factory.setDefaultRequeueRejected(false); // Отключаем автоматическую повторную обработку при ошибках
    return factory;
  }
  
  /**
   * Кастомный обработчик ошибок для логирования деталей ошибок
   */
  public static class CustomErrorHandler extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
    private final Logger logger;
    
    public CustomErrorHandler(Logger logger) {
      this.logger = logger;
    }
    
    @Override
    public boolean isUserCauseFatal(Throwable cause) {
      if (cause instanceof ListenerExecutionFailedException) {
        ListenerExecutionFailedException exception = (ListenerExecutionFailedException) cause;
        logger.error("Ошибка обработки сообщения RabbitMQ: " + exception.getMessage());
        
        // Логирование информации о сообщении при ошибке
        String failedMessage = exception.getFailedMessage() != null ? 
            exception.getFailedMessage().toString() : "неизвестное сообщение";
        logger.error("Проблемное сообщение: " + failedMessage);
      }
      return super.isUserCauseFatal(cause);
    }
  }
}