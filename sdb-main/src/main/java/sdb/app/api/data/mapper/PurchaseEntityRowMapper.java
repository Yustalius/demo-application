package sdb.app.api.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.data.entity.user.UserEntity;

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
    result.setProduct(rs.getString("product"));
    result.setPrice(rs.getInt("price"));

    return result;
  }
}
