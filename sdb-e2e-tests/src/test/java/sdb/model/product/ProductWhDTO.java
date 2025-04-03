package sdb.model.product;

import org.springframework.lang.NonNull;
import sdb.data.entity.products.ProductWhEntity;

public record ProductWhDTO(
    Integer id,
    Integer externalProductId,
    String name,
    Integer stockQuantity
) implements ProductDTO {

  public ProductWhDTO(Integer externalProductId, String name, Integer stockQuantity) {
    this(null, externalProductId, name, stockQuantity);
  }

  public static @NonNull ProductWhDTO fromEntity(@NonNull ProductWhEntity entity) {
    return new ProductWhDTO(
        entity.getId(),
        entity.getExternalProductId(),
        entity.getName(),
        entity.getStockQuantity()
    );
  }

  public static @NonNull ProductWhDTO fromCoreDto(@NonNull ProductCoreDTO dto, int stockQuantity) {
    return new ProductWhDTO(
        null,
        dto.id(),
        dto.productName(),
        stockQuantity
    );
  }
}
