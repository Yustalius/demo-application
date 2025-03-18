package sdb.app.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import sdb.app.data.entity.order.OrderEntity;
import sdb.app.data.entity.order.OrderItemEntity;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.data.repository.*;
import sdb.app.ex.OrderNotFoundException;
import sdb.app.ex.ProductNotFoundException;
import sdb.app.ex.StatusTransitionException;
import sdb.app.ex.UserNotFoundException;
import sdb.app.model.order.OrderDTO;
import sdb.app.model.order.OrderStatus;
import sdb.app.model.order.OrderStatusTransition;
import sdb.app.service.OrderService;
import sdb.app.utils.ProductPriceKey;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final UsersRepository usersRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  @Override
  @Transactional
  public OrderDTO createOrder(OrderDTO order) {
    OrderEntity createdOrder = createOrderEntity(order);
    createOrderItems(createdOrder, groupProductsByIdAndPrice(order));

    return OrderDTO.fromEntity(createdOrder);
  }

  @Override
  @Transactional
  public OrderDTO updateStatus(int orderId, @Nonnull OrderStatus newStatus) {
    OrderEntity order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));

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
  private OrderEntity createOrderEntity(OrderDTO order) {
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
  private Map<ProductPriceKey, Integer> groupProductsByIdAndPrice(OrderDTO order) {
    return Arrays.stream(order.products())
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
          .orElseThrow(() -> new ProductNotFoundException(key.getProductId()));

      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setOrder(order);
      orderItemEntity.setProduct(productEntity);
      orderItemEntity.setQuantity(totalQuantity);
      orderItemEntity.setPrice(key.getPrice());

      OrderItemEntity savedItem = orderItemRepository.save(orderItemEntity);
      order.addOrderItem(savedItem);
    });
  }
}