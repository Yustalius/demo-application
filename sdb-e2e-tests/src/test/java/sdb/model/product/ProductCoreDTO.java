package sdb.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import sdb.data.entity.products.ProductCoreEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductCoreDTO(
    Integer id,
    String productName,
    String description,
    Integer price,
    Boolean isAvailable
) implements ProductDTO {
  public ProductCoreDTO(String productName, String description, Integer price) {
    this(null, productName, description, price, null);
  }

  public static @Nonnull ProductCoreDTO fromEntity(@Nonnull ProductCoreEntity entity) {
    return new ProductCoreDTO(
        entity.getId(),
        entity.getProductName(),
        entity.getDescription(),
        entity.getPrice(),
        entity.getIsAvailable()
    );
  }
}
