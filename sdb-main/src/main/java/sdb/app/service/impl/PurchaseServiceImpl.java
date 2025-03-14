package sdb.app.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.app.data.dao.PurchaseDao;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.entity.purchase.PurchaseEntity;
import sdb.app.data.entity.purchase.PurchaseEntityOld;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.data.repository.OrderRepository;
import sdb.app.data.repository.ProductRepository;
import sdb.app.data.repository.UsersRepository;
import sdb.app.ex.OrderNotFoundException;
import sdb.app.ex.ProductNotFoundException;
import sdb.app.ex.UserNotFoundException;
import sdb.app.logging.Logger;
import sdb.app.model.purchase.OrderDTO;
import sdb.app.service.OrderService;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final UsersRepository usersRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public List<OrderDTO> createOrder(@Nonnull OrderDTO... orders) {
    List<PurchaseEntity> entities = new ArrayList<>();

    for (OrderDTO order : orders) {
      UsersEntity user = usersRepository.findById(order.userId())
          .orElseThrow(() -> new UserNotFoundException(order.userId()));

      ProductEntity product = productRepository.findById(order.productId())
          .orElseThrow(() -> new ProductNotFoundException(order.productId()));

      PurchaseEntity entity = new PurchaseEntity();
      entity.setUser(user);
      entity.setProduct(product);
      entity.setPrice(order.price());
      entity.setTimestamp(System.currentTimeMillis());

      entities.add(entity);
    }

    return orderRepository.saveAll(entities).stream()
        .map(OrderDTO::fromEntity)
        .toList();
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
  public OrderDTO getPurchase(int orderId) {
    return OrderDTO.fromEntity(
        orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId))
    );
  }

  @Override
  @Transactional
  public List<OrderDTO> getUserPurchases(int userId) {
    UsersEntity user = usersRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    return orderRepository.findByUser(user).stream()
        .map(OrderDTO::fromEntity)
        .toList();
  }
}
