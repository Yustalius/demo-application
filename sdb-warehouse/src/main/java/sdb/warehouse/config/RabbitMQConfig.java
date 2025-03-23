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

  // Общий ключ маршрутизации для всех событий заказа
  public static final String ORDER_EVENT_ROUTING_KEY = "order.event";

  // Константы для обменников и очередей
  public static final String WAREHOUSE_EVENTS_EXCHANGE = "warehouse-events-exchange";
  public static final String WAREHOUSE_ORDER_EVENT_QUEUE = "warehouse-order-event-queue";

  // Константы для основных сообщений о заказах
  public static final String CORE_EVENTS_EXCHANGE = "core-events-exchange";

  // Константы для Dead Letter
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
   * Создает Topic Exchange для входящих событий заказа от главного сервиса.
   * Этот тип обмена отправляет сообщения в очереди на основе routing key,
   * что позволяет реализовать гибкую маршрутизацию.
   */
  @Bean
  public TopicExchange coreEventsExchange() {
    return new TopicExchange(CORE_EVENTS_EXCHANGE);
  }

  /**
   * Создает Topic Exchange для исходящих событий склада.
   * Через этот обмен сервис склада отправляет сообщения другим сервисам,
   * но не себе.
   */
  @Bean
  public TopicExchange warehouseEventsExchange() {
    return new TopicExchange(WAREHOUSE_EVENTS_EXCHANGE);
  }

  /**
   * Создает очередь для обработки событий заказа в сервисе склада
   * с настройкой перенаправления в DLQ при ошибках
   */
  @Bean
  public Queue warehouseOrderEventQueue() {
    return QueueBuilder.durable(WAREHOUSE_ORDER_EVENT_QUEUE)
        .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
        .withArgument("x-dead-letter-routing-key", "deadLetter")
        .withArgument("x-message-ttl", 30000) // 30 секунд TTL
        .build();
  }

  /**
   * Связывает очередь склада с core exchange используя специфический routing key
   * для событий заказа
   */
  @Bean
  public Binding warehouseOrderEventBinding() {
    return BindingBuilder
        .bind(warehouseOrderEventQueue())
        .to(coreEventsExchange())
        .with(ORDER_EVENT_ROUTING_KEY);
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