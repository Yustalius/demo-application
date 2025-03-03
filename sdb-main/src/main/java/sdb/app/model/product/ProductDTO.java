package sdb.app.model.product;

import jakarta.annotation.Nonnull;
import sdb.app.data.entity.product.ProductEntity;

public record ProductDTO(
    Integer id,
    String productName,
    String description
) {
  public static @Nonnull ProductDTO fromEntity(@Nonnull ProductEntity entity) {
    return new ProductDTO(
        entity.getId(),
        entity.getProductName(),
        entity.getDescription()
    );
  }
}
