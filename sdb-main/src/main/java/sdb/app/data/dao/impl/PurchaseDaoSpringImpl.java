package sdb.app.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sdb.app.data.dao.PurchaseDao;
import sdb.app.data.entity.purchase.PurchaseEntity;
import sdb.app.data.mapper.PurchaseEntityRowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
public class PurchaseDaoSpringImpl implements PurchaseDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public PurchaseEntity createPurchase(PurchaseEntity purchase) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO purchases (user_id, product_id, price, \"timestamp\") VALUES (?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setObject(1, purchase.getUserId());
      ps.setInt(2, purchase.getProductId());
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
    return Optional.of(
        jdbcTemplate.query(
            "SELECT * FROM \"purchases\"",
            PurchaseEntityRowMapper.instance
        ));
  }

  @Override
  public Optional<PurchaseEntity> getPurchase(int purchaseId) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"purchases\" WHERE purchase_id = ?",
            PurchaseEntityRowMapper.instance,
            purchaseId
        ));
  }

  @Override
  public Optional<List<PurchaseEntity>> getUserPurchases(int userId) {
    return Optional.of(jdbcTemplate.query(
        "SELECT * FROM \"purchases\" WHERE user_id = ?",
        PurchaseEntityRowMapper.instance,
        userId
    ));
  }
}
