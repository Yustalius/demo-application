package sdb.app.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.app.config.RabbitMQConfig;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.entity.order.OrderEntity;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.data.repository.OrderRepository;
import sdb.app.data.repository.ProductRepository;
import sdb.app.data.repository.UsersRepository;
import sdb.app.ex.OrderNotFoundException;
import sdb.app.ex.ProductNotFoundException;
import sdb.app.ex.UserNotFoundException;
import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderStatus;
import sdb.app.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final UsersRepository usersRepository;
  private final ProductRepository productRepository;
  private final RabbitTemplate rabbit;

  @Override
  @Transactional
  public List<OrderDTO> createOrder(@Nonnull OrderDTO... orders) {
    List<OrderDTO> createdOrders = orderRepository.saveAll(convertOrdersToEntities(orders)).stream()
        .map(OrderDTO::fromEntity)
        .toList();

    for (OrderDTO order : createdOrders) {
      rabbit.convertAndSend(
          RabbitMQConfig.ORDER_EXCHANGE,
          RabbitMQConfig.ROUTING_KEY_PENDING,
          order
      );
    }

    return createdOrders;
  }

  @Override
  @Transactional
  public OrderDTO updateStatus(int orderId, OrderStatus status) {
    return OrderDTO.fromEntity(
        orderRepository.findById(orderId)
            .map(order -> {
              order.setStatus(status);
              return orderRepository.save(order);
            })
            .orElseThrow(() -> new OrderNotFoundException(orderId))
    );
  }

  @Override
  @Deprecated
  @Transactional(readOnly = true)
  public List<OrderDTO> getOrders() {
    return orderRepository.findAllWithJoins().stream()
        .map(OrderDTO::fromEntity)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDTO getOrder(int orderId) {
    return OrderDTO.fromEntity(
        orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId))
    );
  }

  @Override
  @Transactional
  public List<OrderDTO> getUserOrders(int userId) {
    UsersEntity user = usersRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    return orderRepository.findByUser(user).stream()
        .map(OrderDTO::fromEntity)
        .toList();
  }

  private List<OrderEntity> convertOrdersToEntities(OrderDTO[] orders) {
    List<OrderEntity> entities = new ArrayList<>();
    for (OrderDTO order : orders) {
      UsersEntity user = usersRepository.findById(order.userId())
          .orElseThrow(() -> new UserNotFoundException(order.userId()));

      ProductEntity product = productRepository.findById(order.productId())
          .orElseThrow(() -> new ProductNotFoundException(order.productId()));

      OrderEntity entity = new OrderEntity();
      entity.setUser(user);
      entity.setProduct(product);
      entity.setPrice(order.price());
      entity.setTimestamp(System.currentTimeMillis());
      entity.setStatus(OrderStatus.PENDING);

      entities.add(entity);
    }

    return entities;
  }
}
