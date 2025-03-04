package sdb.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import sdb.data.entity.products.ProductEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
