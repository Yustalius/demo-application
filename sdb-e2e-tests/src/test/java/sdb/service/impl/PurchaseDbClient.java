package sdb.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sdb.config.Config;
import sdb.data.entity.purchases.PurchaseEntity;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseDbClient {
  private static final Config CFG = Config.getInstance();

  private final JdbcTemplate jdbcTemplate;

  public PurchaseDbClient(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

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

/*  List<PurchaseJson> getPurchases() {

  }

  PurchaseJson getPurchase(int purchaseId) {

  }

  List<PurchaseJson> getUserPurchases(int userId) {

  }*/
}
