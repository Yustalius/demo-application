package sdb.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import sdb.data.entity.products.ProductEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductEntityRowMapper implements RowMapper<ProductEntity> {
  public static final ProductEntityRowMapper instance = new ProductEntityRowMapper();

  private ProductEntityRowMapper() {
  }

  @Override
  public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    ProductEntity result = new ProductEntity();
    result.setId(rs.getInt("id"));
    result.setProductName(rs.getString("product_name"));
    result.setDescription(rs.getString("description"));
    result.setPrice(rs.getInt("price"));

    return result;
  }
}
