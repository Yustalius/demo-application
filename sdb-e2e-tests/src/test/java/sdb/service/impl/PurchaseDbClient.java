package sdb.service.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;
import retrofit2.Response;
import sdb.api.PurchaseApi;
import sdb.api.core.RestClient;
import sdb.config.Config;
import sdb.data.dao.AuthDao;
import sdb.data.dao.UserDao;
import sdb.data.dao.impl.AuthDaoSpringImpl;
import sdb.data.dao.impl.UserDaoSpringImpl;
import sdb.data.entity.product.PurchaseEntity;
import sdb.model.product.PurchaseJson;
import sdb.service.PurchaseClient;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

/*  List<PurchaseJson> getPurchases() {

  }

  PurchaseJson getPurchase(int purchaseId) {

  }

  List<PurchaseJson> getUserPurchases(int userId) {

  }*/
}
