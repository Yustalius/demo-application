package sdb.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sdb.config.Config;
import sdb.data.dao.ProductWhDao;
import sdb.data.entity.products.ProductWhEntity;
import sdb.data.mapper.ProductCoreEntityRowMapper;
import sdb.data.mapper.ProductWhEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static sdb.data.Databases.dataSource;

public class ProductWhDaoImpl implements ProductWhDao {
  private final JdbcTemplate jdbcTemplate;

  public ProductWhDaoImpl() {
    this.jdbcTemplate = new JdbcTemplate(dataSource(Config.getInstance().whDbUrl()));
  }

  @Override
  public ProductWhEntity create(ProductWhEntity entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO \"products\" (external_product_id, name, stock_quantity) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setInt(1, entity.getExternalProductId());
      ps.setString(2, entity.getName());
      ps.setInt(3, entity.getStockQuantity());
      return ps;
    }, keyHolder);

    entity.setId((Integer) requireNonNull(keyHolder.getKeys().get("id")));
    return entity;
  }

  @Override
  public void update(int productId, ProductWhEntity entity) {
    StringBuilder sql = new StringBuilder("UPDATE products SET ");
    List<Object> params = new ArrayList<>();

    if (entity.getExternalProductId() != null) {
      sql.append("external_product_id = ?, ");
      params.add(entity.getExternalProductId());
    }

    if (entity.getName() != null) {
      sql.append("name = ?, ");
      params.add(entity.getName());
    }

    if (entity.getStockQuantity() != null) {
      sql.append("stock_quantity = ?, ");
      params.add(entity.getStockQuantity());
    }

    if (params.isEmpty()) {
      throw new IllegalArgumentException("No fields to update");
    }
    sql.setLength(sql.length() - 2);
    sql.append(" WHERE id = ?");
    params.add(productId);

    jdbcTemplate.update(sql.toString(), params.toArray());
  }

  @Override
  public Optional<ProductWhEntity> get(int productId) {
    List<ProductWhEntity> results = jdbcTemplate.query(
        "SELECT * FROM products WHERE id = ?",
        ProductWhEntityRowMapper.instance,
        productId
    );

    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }

  @Override
  public Optional<List<ProductWhEntity>> getAll() {
    return Optional.of(
        jdbcTemplate.query(
            "SELECT * FROM products",
            ProductWhEntityRowMapper.instance
        ));
  }

  @Override
  public void delete(int productId) {
    jdbcTemplate.update("DELETE FROM products WHERE id = ?", productId);
  }
}
