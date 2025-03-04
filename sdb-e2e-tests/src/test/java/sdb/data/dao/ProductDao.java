package sdb.data.dao;

import sdb.data.entity.products.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

  ProductEntity create(ProductEntity entity);

  void update(int id, ProductEntity entity);

  Optional<ProductEntity> get(int id);

  Optional<List<ProductEntity>> getAll();

  void delete(int id);
}
