package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.products.ProductCoreEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCoreEntityRowMapper implements RowMapper<ProductCoreEntity> {
  public static final ProductCoreEntityRowMapper instance = new ProductCoreEntityRowMapper();

  private ProductCoreEntityRowMapper() {
  }

  @Override
  public ProductCoreEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    ProductCoreEntity result = new ProductCoreEntity();
    result.setId(rs.getInt("id"));
    result.setProductName(rs.getString("product_name"));
    result.setDescription(rs.getString("description"));
    result.setPrice(rs.getInt("price"));
    result.setIsAvailable(rs.getBoolean("is_available"));

    return result;
  }
}
