package sdb.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.data.entity.orders.OrderEntity;
import sdb.data.entity.orders.OrderItemEntity;
import sdb.data.entity.products.ProductEntity;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.model.order.OrderStatus.PENDING;

public class OrderDbClient {
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;

  public OrderDbClient(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.transactionTemplate = new TransactionTemplate(new JdbcTransactionManager(dataSource));
  }

  public Integer createOrderWithOrderItems(OrderEntity order, OrderItemEntity... orderItems) {
    return transactionTemplate.execute(status -> {
      Integer orderId = createOrder(order);
      order.setOrderId(orderId);
      
      insertOrderItems(orderId, orderItems);

      return orderId;
    });
  }

//  List<PurchaseJson> getPurchases() {
//
//  }

  @Nullable
  public OrderEntity getOrder(int orderId) {
    // Запрос на получение информации о заказе
    OrderEntity order = jdbcTemplate.queryForObject(
        "SELECT * FROM orders WHERE order_id = ?",
        (rs, rowNum) -> {
          OrderEntity entity = new OrderEntity();
          entity.setOrderId(rs.getInt("order_id"));
          entity.setStatus(PENDING);
          entity.setUserId(rs.getInt("user_id"));
          entity.setCreatedAt(rs.getDate("created_at"));
          return entity;
        },
        orderId
    );

    if (order == null) {
      return null;
    } 

    // Запрос на получение элементов заказа
    List<OrderItemEntity> orderItems = jdbcTemplate.query(
        "SELECT * FROM order_items WHERE order_id = ?",
        (rs, rowNum) -> {
          OrderItemEntity item = new OrderItemEntity();
          item.setOrderItemId(rs.getInt("order_item_id"));
          
          ProductEntity product = new ProductEntity();
          product.setId(rs.getInt("product_id"));
          item.setProduct(product);
          
          item.setQuantity(rs.getInt("quantity"));
          item.setPrice(rs.getInt("price"));
          return item;
        },
        orderId
    );

    if (!orderItems.isEmpty()) {
      order.setOrderItems(orderItems);
    }

    return order;
  }

//  List<PurchaseJson> getUserPurchases(int userId) {
//
//  }

  private Integer createOrder(OrderEntity order) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO orders (user_id, status) VALUES (?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setObject(1, order.getUser().getId());
      ps.setString(2, order.getStatus().toString());
      return ps;
    }, keyHolder);
    return (Integer) keyHolder.getKeys().get("order_id");
  }

  private void insertOrderItems(Integer orderId, OrderItemEntity... orderItems) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, orderId);
            ps.setInt(2, orderItems[i].getProduct().getId());
            ps.setInt(3, orderItems[i].getQuantity());
            ps.setInt(4, orderItems[i].getPrice());
          }

          @Override
          public int getBatchSize() {
            return orderItems.length;
          }
        }
    );
  }
}
