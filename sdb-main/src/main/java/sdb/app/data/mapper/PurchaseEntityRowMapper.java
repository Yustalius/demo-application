package sdb.app.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.app.data.entity.order.OrderEntityOld;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseEntityRowMapper implements RowMapper<OrderEntityOld> {

  public static final PurchaseEntityRowMapper instance = new PurchaseEntityRowMapper();

  private PurchaseEntityRowMapper() {
  }

  @Override
  public OrderEntityOld mapRow(ResultSet rs, int rowNum) throws SQLException {
    OrderEntityOld result = new OrderEntityOld();
    result.setPurchaseId(rs.getInt("purchase_id"));
    result.setUserId(rs.getInt("user_id"));
    result.setProductId(rs.getInt("product_id"));
    result.setPrice(rs.getInt("price"));
    result.setTimestamp(rs.getLong("timestamp"));

    return result;
  }
}
