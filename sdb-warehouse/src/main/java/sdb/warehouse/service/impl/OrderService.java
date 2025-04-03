package sdb.warehouse.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.data.entity.OrderEntity;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.data.repository.OrderRepository;
import sdb.warehouse.ex.OrderNotFoundException;
import sdb.warehouse.model.order.OrderItemDTO;
import sdb.warehouse.model.order.OrderStatus;
import sdb.warehouse.model.product.ProductDTO;
import sdb.warehouse.service.ProductService;
import utils.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sdb.warehouse.model.order.OrderStatus.NEW;

/**
 * Сервис для обработки заказов на складе
 */
@Service
@RequiredArgsConstructor
public class OrderService {

  private final Logger logger;
  private final OrderRepository orderRepository;
  private final ProductService productService;

  @Transactional
  public Map<OrderItemDTO, ProductDTO> findProductsByDto(List<OrderItemDTO> items) {
    Map<OrderItemDTO, ProductDTO> orderItemProductEntityMap = new HashMap<>();
    for (OrderItemDTO item : items) {
      ProductDTO productEntity = productService.getByExternalId(item.productId());

      orderItemProductEntityMap.put(item, productEntity);
    }

    return orderItemProductEntityMap;
  }

  /**
   * Создает новые заказы в системе склада
   *
   * @param productsWithQuantity карта товаров и их количества для заказа
   * @param externalOrderId внешний идентификатор заказа
   */
  @Transactional
  public void createOrders(Map<ProductDTO, Integer> productsWithQuantity, Integer externalOrderId) {
    for (ProductDTO product : productsWithQuantity.keySet()) {
      Integer orderQuantity = productsWithQuantity.get(product);
      Integer currentStock = product.stockQuantity();

      try {
        // уменьшаем количество товара
        productService.addProductQuantity(product.externalProductId(), -orderQuantity);
        
        OrderEntity orderEntity = OrderEntity.builder()
            .externalOrderId(externalOrderId)
            .product(ProductEntity.fromDTO(product))
            .quantity(orderQuantity)
            .status(NEW.name())
            .build();
        orderRepository.save(orderEntity);

        logger.info(String.format("Order saved for product: %s, stock reduced from %s to %s",
            product.externalProductId(), currentStock, currentStock - orderQuantity));
      } catch (Exception e) {
        logger.error(String.format("Error saving order or updating stock for product: %s. Error: %s",
            product.externalProductId(), e.getMessage(), e));
        throw new RuntimeException("Error processing order for product: " + product.externalProductId(), e);
      }
    }
  }

  /**
   * Обновляет статус заказа
   *
   * @param orderId ID заказа
   * @param status новый статус
   * @return true если заказ найден и обновлен, false в противном случае
   */
  @Transactional
  public boolean updateOrderStatus(Integer orderId, OrderStatus status) {
    OrderEntity order = orderRepository.findById(orderId).orElse(null);
    if (order == null) {
      logger.warn("Order with ID %s not found".formatted(orderId));
      return false;
    }

    order.setStatus(status.name());
    orderRepository.save(order);
    logger.info("Order %s status updated to %s".formatted(orderId, status));
    return true;
  }

  /**
   * Обновляет статус заказа по внешнему ID
   *
   * @param externalOrderId внешний ID заказа
   * @param status новый статус
   * @return true если заказ найден и обновлен, false в противном случае
   */
  @Transactional
  public void updateOrderStatusByExternalId(Integer externalOrderId, OrderStatus status) {
    List<OrderEntity> orders = orderRepository.findByExternalOrderId(externalOrderId);
    if (orders.isEmpty()) {
      logger.warn("Order with external ID %s not found".formatted(externalOrderId));
      throw new OrderNotFoundException("Error while updating order status", externalOrderId);
    }

    for (OrderEntity orderEntity : orders) {
      orderEntity.setStatus(status.name());
    }
    orderRepository.saveAll(orders);

    logger.info("Orders with external ID %s status updated to %s".formatted(externalOrderId, status));
  }
}