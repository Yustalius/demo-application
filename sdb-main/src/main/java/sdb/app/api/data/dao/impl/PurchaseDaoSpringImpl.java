package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.data.mapper.PurchaseEntityRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
public class PurchaseDaoSpringImpl implements PurchaseDao {

  //todo вынести JdbcTemplate в переменную
  private final DataSource dataSource;

  public PurchaseDaoSpringImpl(@Qualifier("dbDatasource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // todo переписать на единственный метод, который возвращает готовую покупку и возвращать список добавленных
  @Override
  public PurchaseEntity createPurchase(PurchaseEntity purchase) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO purchases (user_id, product, price, \"timestamp\") VALUES (?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setObject(1, purchase.getUserId());
      ps.setString(2, purchase.getProduct());
      ps.setInt(3, purchase.getPrice());
      ps.setLong(4, System.currentTimeMillis());
      return ps;
    }, keyHolder);

    purchase.setPurchaseId((Integer) keyHolder.getKeys().get("purchase_id"));
    purchase.setTimestamp((Long) keyHolder.getKeys().get("timestamp"));

    return purchase;
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
