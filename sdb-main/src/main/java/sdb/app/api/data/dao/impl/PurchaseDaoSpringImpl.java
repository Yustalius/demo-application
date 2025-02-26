package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sdb.app.api.data.Databases;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.data.mapper.PurchaseEntityRowMapper;
import sdb.app.api.data.mapper.UserEntityRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
public class PurchaseDaoSpringImpl implements PurchaseDao {

  private final DataSource dataSource;

  public PurchaseDaoSpringImpl(@Qualifier("dbDatasource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void createPurchase(PurchaseEntity... purchases) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.batchUpdate(
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
  public List<PurchaseEntity> getPurchases() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
            "SELECT * FROM \"purchases\"",
            PurchaseEntityRowMapper.instance
        );
  }

  @Override
  public PurchaseEntity getPurchase(int purchaseId) {
    return null;
  }

  @Override
  public PurchaseEntity getUserPurchases(int userId) {
    return null;
  }
}
