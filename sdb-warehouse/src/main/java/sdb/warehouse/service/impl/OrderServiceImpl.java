package sdb.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.data.entity.OrderEntity;
import sdb.warehouse.data.entity.ProductEntity;
import sdb.warehouse.data.repository.OrderRepository;
import sdb.warehouse.data.repository.ProductRepository;
import sdb.warehouse.model.event.OrderEvent;
import sdb.warehouse.model.order.OrderItemDTO;
import sdb.warehouse.model.order.OrderStatus;
import utils.ex.ProductNotFoundException;
import utils.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static sdb.warehouse.model.order.OrderStatus.NEW;

/**
 * Сервис для обработки заказов на складе
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

  private final Logger logger;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  @Transactional
  public Map<OrderItemDTO, ProductEntity> findProductsInDatabaseByDto(OrderEvent event) {
    Map<OrderItemDTO, ProductEntity> orderItemProductEntityMap = new HashMap<>();
    for (OrderItemDTO item : event.getItems()) {
      ProductEntity productEntity = productRepository.findByExternalProductId(item.productId())
          .orElseThrow(() -> new ProductNotFoundException(
              "Error during processing order in warehouse",
              item.productId()));

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
  public void createOrders(Map<ProductEntity, Integer> productsWithQuantity, Integer externalOrderId) {
    for (ProductEntity product : productsWithQuantity.keySet()) {
      Integer orderQuantity = productsWithQuantity.get(product);
      Integer currentStock = product.getStockQuantity();
      try {
        // уменьшаем количество товара
        product.setStockQuantity(currentStock - orderQuantity);
        productRepository.save(product);
        
        OrderEntity orderEntity = OrderEntity.builder()
            .externalOrderId(externalOrderId)
            .product(product)
            .quantity(orderQuantity)
            .status(NEW.name())
            .build();

        orderRepository.save(orderEntity);
        logger.info(String.format("Order saved for product: %s, stock reduced from %s to %s", 
            product.getExternalProductId(), currentStock, product.getStockQuantity()));
      } catch (Exception e) {
        logger.error(String.format("Error saving order or updating stock for product: %s. Error: %s", 
            product.getExternalProductId(), e.getMessage(), e));
        // В случае ошибки откатываем транзакцию (произойдет автоматически из-за @Transactional)
        throw new RuntimeException("Error processing order for product: " + product.getExternalProductId(), e);
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
  public boolean updateOrderStatusByExternalId(Integer externalOrderId, OrderStatus status) {
    OrderEntity order = orderRepository.findByExternalOrderId(externalOrderId);
    if (order == null) {
      logger.warn("Order with external ID %s not found".formatted(externalOrderId));
      return false;
    }

    order.setStatus(status.name());
    orderRepository.save(order);
    logger.info("Order with external ID %s status updated to %s".formatted(externalOrderId, status));
    return true;
  }
}