package sdb.service.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseDbClient {
  private static final Config CFG = Config.getInstance();

  private final JdbcTemplate jdbcTemplate;

  public PurchaseDbClient(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void createPurchase(PurchaseEntity... purchases) {
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

/*  List<PurchaseJson> getPurchases() {

  }

  PurchaseJson getPurchase(int purchaseId) {

  }

  List<PurchaseJson> getUserPurchases(int userId) {

  }*/
}
