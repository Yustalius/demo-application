package sdb.core.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.data.entity.order.OrderEntity;
import sdb.core.data.entity.order.OrderItemEntity;
import sdb.core.data.entity.product.ProductEntity;
import sdb.core.data.entity.user.UsersEntity;
import sdb.core.data.repository.OrderItemRepository;
import sdb.core.data.repository.OrderRepository;
import sdb.core.data.repository.ProductRepository;
import sdb.core.data.repository.UsersRepository;
import sdb.core.ex.OrderNotFoundException;
import utils.ex.ProductNotFoundException;
import sdb.core.ex.StatusTransitionException;
import sdb.core.ex.UserNotFoundException;
import sdb.core.model.event.OrderEvent;
import sdb.core.model.order.CreateOrderDTO;
import sdb.core.model.order.OrderDTO;
import sdb.core.model.order.OrderStatus;
import sdb.core.model.order.OrderStatusTransition;
import sdb.core.service.EventPublisher;
import sdb.core.service.OrderService;
import sdb.core.utils.ProductPriceKey;
import utils.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final UsersRepository usersRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final EventPublisher eventPublisher;
  private final Logger logger;

  @Override
  @Transactional
  public OrderDTO createOrder(CreateOrderDTO order) {
    OrderEntity createdOrder = createOrderEntity(order);
    createOrderItems(createdOrder, groupProductsByIdAndPrice(order));

    OrderDTO createdOrderDTO = OrderDTO.fromEntity(createdOrder);
    
    eventPublisher.publishOrderCreatedEvent(createdOrderDTO);
    
    return createdOrderDTO;
  }

  @Override
  @Transactional
  public OrderDTO updateStatus(int orderId, @Nonnull OrderStatus newStatus) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Error while updating status for order " + orderId, orderId));

    logger.info(String.format(
        "Changing order id = %s status from %s to %s",
        orderId, order.getStatus(), newStatus
    ));
    OrderStatus currentStatus = order.getStatus();
    if (currentStatus == newStatus) {
      return OrderDTO.fromEntity(order);
    }

    if (!OrderStatusTransition.isTransitionAllowed(currentStatus, newStatus)) {
      throw new StatusTransitionException(currentStatus, newStatus);
    }

    order.setStatus(newStatus);
    OrderEntity updatedOrder = orderRepository.save(order);

    return OrderDTO.fromEntity(updatedOrder);
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDTO> getOrders() {
    return orderRepository.findAll().stream()
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
  @Transactional(readOnly = true)
  public List<OrderDTO> getUserOrders(int userId) {
    UsersEntity user = usersRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    return orderRepository.findByUser(user).stream()
        .map(OrderDTO::fromEntity)
        .toList();
  }

  /**
   * Создает и сохраняет сущность заказа
   */
  private OrderEntity createOrderEntity(CreateOrderDTO order) {
    UsersEntity user = usersRepository.findById(order.userId())
        .orElseThrow(() -> new UserNotFoundException(order.userId()));

    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUser(user);
    orderEntity.setStatus(OrderStatus.PENDING);

    return orderRepository.save(orderEntity);
  }

  /**
   * Группирует продукты по ID и цене, суммируя количество
   */
  private Map<ProductPriceKey, Integer> groupProductsByIdAndPrice(CreateOrderDTO order) {
    return order.products().stream()
        .collect(Collectors.toMap(
            product -> new ProductPriceKey(product.productId(), product.price()),
            product -> product.quantity(),
            Integer::sum
        ));
  }

  /**
   * Создает и сохраняет элементы заказа для каждой уникальной комбинации ID и цены
   */
  private void createOrderItems(OrderEntity order, Map<ProductPriceKey, Integer> productQuantities) {
    productQuantities.forEach((key, totalQuantity) -> {
      ProductEntity productEntity = productRepository.findById(key.getProductId())
          .orElseThrow(() -> new ProductNotFoundException(
            "Error while creating order items for order " + order,
            key.getProductId()));

      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setOrder(order);
      orderItemEntity.setProduct(productEntity);
      orderItemEntity.setQuantity(totalQuantity);
      orderItemEntity.setPrice(key.getPrice());

      OrderItemEntity savedItem = orderItemRepository.save(orderItemEntity);
      order.addOrderItem(savedItem);
    });
    logger.info("Order items created for order ID = " + order);
  }
}