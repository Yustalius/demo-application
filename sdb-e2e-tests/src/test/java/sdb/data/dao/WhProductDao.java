package sdb.data.dao;

import sdb.data.entity.products.ProductWhEntity;

import java.util.List;
import java.util.Optional;

public interface WhProductDao {

  ProductWhEntity create(ProductWhEntity entity);

  void update(int id, ProductWhEntity entity);

  Optional<ProductWhEntity> get(int id);

  Optional<ProductWhEntity> getByExternalId(int id);

  Optional<List<ProductWhEntity>> getAll();

  void delete(int id);
}
