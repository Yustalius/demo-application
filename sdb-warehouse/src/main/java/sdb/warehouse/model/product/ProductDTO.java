package sdb.warehouse.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import sdb.warehouse.data.entity.ProductEntity;

public record ProductDTO(
    @JsonIgnore
    Integer id,
    @JsonProperty("id")
    Integer externalProductId,
    String name,
    Integer stockQuantity
) {
  public ProductDTO(Integer externalProductId, String name, Integer stockQuantity) {
    this(null, externalProductId, name, stockQuantity);
  }

  public static ProductDTO fromEntity(ProductEntity entity) {
    return new ProductDTO(
        entity.getId(),
        entity.getExternalProductId(),
        entity.getName(),
        entity.getStockQuantity()
    );
  }
}
