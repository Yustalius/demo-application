package sdb.core.model.product;

import jakarta.annotation.Nonnull;
import sdb.core.data.entity.product.ProductEntity;

public record ProductDTO(
    Integer id,
    String productName,
    String description,
    Integer price
) {
  public static @Nonnull ProductDTO fromEntity(@Nonnull ProductEntity entity) {
    return new ProductDTO(
        entity.getId(),
        entity.getProductName(),
        entity.getDescription(),
        entity.getPrice()
    );
  }
}
