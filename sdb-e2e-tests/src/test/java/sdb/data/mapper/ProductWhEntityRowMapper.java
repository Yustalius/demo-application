package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.products.ProductCoreEntity;
import sdb.data.entity.products.ProductWhEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductWhEntityRowMapper implements RowMapper<ProductWhEntity> {
  public static final ProductWhEntityRowMapper instance = new ProductWhEntityRowMapper();

  private ProductWhEntityRowMapper() {
  }

  @Override
  public ProductWhEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    ProductWhEntity result = new ProductWhEntity();
    result.setId(rs.getInt("id"));
    result.setExternalProductId(rs.getInt("external_product_id"));
    result.setName(rs.getString("name"));
    result.setStockQuantity(rs.getInt("stock_quantity"));

    return result;
  }
}
