package sdb.data.repositrory;

import jakarta.annotation.Nonnull;
import sdb.data.entity.products.ProductEntity;

import java.util.Optional;

public interface ProductRepository {

  @Nonnull
  static ProductRepository getInstance() {
    return new ProductRepositoryHibernate();
  }

  @Nonnull
  ProductEntity create(ProductEntity product);

  @Nonnull
  Optional<ProductEntity> findById(int id);

  @Nonnull
  Optional<ProductEntity> findByName(String productName);
}
