package sdb.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sdb.config.Config;
import sdb.data.dao.ProductDao;
import sdb.data.entity.products.ProductEntity;
import sdb.data.mapper.ProductEntityRowMapper;
import sdb.data.mapper.PurchaseEntityRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static sdb.data.Databases.dataSource;

public class ProductDaoImpl implements ProductDao {
  private final JdbcTemplate jdbcTemplate;

  public ProductDaoImpl() {
    this.jdbcTemplate = new JdbcTemplate(dataSource(Config.getInstance().postgresUrl()));
  }

  @Override
  public ProductEntity create(ProductEntity entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO \"products\" (product_name, description, price, is_available) VALUES (?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, entity.getProductName());
      ps.setString(2, entity.getDescription());
      ps.setInt(3, entity.getPrice());
      ps.setBoolean(4, entity.getIsAvailable());
      return ps;
    }, keyHolder);

    entity.setId((Integer) requireNonNull(keyHolder.getKeys().get("id")));
    return entity;
  }

  @Override
  public void update(int productId, ProductEntity entity) {
    StringBuilder sql = new StringBuilder("UPDATE products SET ");
    List<Object> params = new ArrayList<>();

    if (entity.getProductName() != null) {
      sql.append("product_name = ?, ");
      params.add(entity.getProductName());
    }

    if (entity.getDescription() != null) {
      sql.append("description = ?, ");
      params.add(entity.getDescription());
    }

    if (entity.getPrice() != null) {
      sql.append("price = ?, ");
      params.add(entity.getPrice());
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
  public Optional<ProductEntity> get(int productId) {
    List<ProductEntity> results = jdbcTemplate.query(
        "SELECT * FROM products WHERE id = ?",
        ProductEntityRowMapper.instance,
        productId
    );

    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }

  @Override
  public Optional<List<ProductEntity>> getAll() {
    return Optional.of(
        jdbcTemplate.query(
            "SELECT * FROM \"products\"",
            ProductEntityRowMapper.instance
        ));
  }

  @Override
  public void delete(int productId) {
    jdbcTemplate.update("DELETE FROM products WHERE id = ?", productId);
  }
}
