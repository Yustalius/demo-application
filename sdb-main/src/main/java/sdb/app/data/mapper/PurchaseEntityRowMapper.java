package sdb.app.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.app.data.entity.purchase.PurchaseEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseEntityRowMapper implements RowMapper<PurchaseEntity> {

  public static final PurchaseEntityRowMapper instance = new PurchaseEntityRowMapper();

  private PurchaseEntityRowMapper() {
  }

  @Override
  public PurchaseEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    PurchaseEntity result = new PurchaseEntity();
    result.setPurchaseId(rs.getInt("purchase_id"));
    result.setUserId(rs.getInt("user_id"));
    result.setProductId(rs.getInt("product_id"));
    result.setPrice(rs.getInt("price"));
    result.setTimestamp(rs.getLong("timestamp"));

    return result;
  }
}
