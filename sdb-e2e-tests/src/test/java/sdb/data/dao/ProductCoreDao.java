package sdb.data.dao;

import sdb.data.entity.products.ProductCoreEntity;

import java.util.List;
import java.util.Optional;

public interface ProductCoreDao {

  ProductCoreEntity create(ProductCoreEntity entity);

  void update(int id, ProductCoreEntity entity);

  Optional<ProductCoreEntity> get(int id);

  Optional<List<ProductCoreEntity>> getAll();

  void delete(int id);
}
