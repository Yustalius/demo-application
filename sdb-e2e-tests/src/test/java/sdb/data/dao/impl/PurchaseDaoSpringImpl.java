package sdb.data.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import sdb.data.dao.PurchaseDao;
import sdb.data.entity.product.PurchaseEntity;
import sdb.data.mapper.PurchaseEntityRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PurchaseDaoSpringImpl implements PurchaseDao {

  private final DataSource dataSource;

  public PurchaseDaoSpringImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // todo переписать на единственный метод, который возвращает готовую покупку и возвращать список добавленных
  @Override
  public void createPurchase(PurchaseEntity... purchases) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    int[] ints = jdbcTemplate.batchUpdate(
        "INSERT INTO purchases (user_id, product, price) VALUES (?, ?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, purchases[i].getUserId());
            ps.setString(2, purchases[i].getProduct());
            ps.setInt(3, purchases[i].getPrice());
          }

          @Override
          public int getBatchSize() {
            return purchases.length;
          }
        }
    );
  }

  @Override
  public Optional<List<PurchaseEntity>> getPurchases() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.of(
        jdbcTemplate.query(
            "SELECT * FROM \"purchases\"",
            PurchaseEntityRowMapper.instance
        ));
  }

  @Override
  public Optional<PurchaseEntity> getPurchase(int purchaseId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"purchases\" WHERE purchase_id = ?",
            PurchaseEntityRowMapper.instance,
            purchaseId
        ));
  }

  @Override
  public Optional<List<PurchaseEntity>> getUserPurchases(int userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.of(jdbcTemplate.query(
        "SELECT * FROM \"purchases\" WHERE user_id = ?",
        PurchaseEntityRowMapper.instance,
        userId
    ));
  }
}
