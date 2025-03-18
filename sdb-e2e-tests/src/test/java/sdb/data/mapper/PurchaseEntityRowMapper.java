package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.orders.OrderEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseEntityRowMapper implements RowMapper<OrderEntity> {

  public static final PurchaseEntityRowMapper instance = new PurchaseEntityRowMapper();

  private PurchaseEntityRowMapper() {
  }

  @Override
  public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    OrderEntity result = new OrderEntity();
//    result.setPurchaseId(rs.getInt("purchase_id"));
//    result.setUserId(rs.getInt("user_id"));
//    result.setProductId(rs.getInt("product"));
//    result.setPrice(rs.getInt("price"));
//    result.setTimestamp(rs.getLong("timestamp"));

    return result;
  }
}
