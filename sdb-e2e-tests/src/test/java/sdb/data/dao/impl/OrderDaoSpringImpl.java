package sdb.data.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import sdb.data.dao.OrderDao;
import sdb.data.entity.orders.OrderEntity;
import sdb.data.mapper.PurchaseEntityRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderDaoSpringImpl implements OrderDao {

  private final DataSource dataSource;

  public OrderDaoSpringImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // todo переписать на единственный метод, который возвращает готовую покупку и возвращать список добавленных
  @Override
  public void createPurchase(OrderEntity... purchases) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.batchUpdate(
        "INSERT INTO orders (user_id, product_id, price, timestamp) VALUES (?, ?, ?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, purchases[i].getUserId());
            ps.setInt(2, purchases[i].getProductId());
            ps.setInt(3, purchases[i].getPrice());
            ps.setLong(4, purchases[i].getTimestamp());
          }

          @Override
          public int getBatchSize() {
            return purchases.length;
          }
        }
    );
  }

  @Override
  public Optional<List<OrderEntity>> getPurchases() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.of(
        jdbcTemplate.query(
            "SELECT * FROM \"orders\"",
            PurchaseEntityRowMapper.instance
        ));
  }

  @Override
  public Optional<OrderEntity> getPurchase(int purchaseId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"orders\" WHERE purchase_id = ?",
            PurchaseEntityRowMapper.instance,
            purchaseId
        ));
  }

  @Override
  public Optional<List<OrderEntity>> getUserPurchases(int userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.of(jdbcTemplate.query(
        "SELECT * FROM \"orders\" WHERE user_id = ?",
        PurchaseEntityRowMapper.instance,
        userId
    ));
  }
}
