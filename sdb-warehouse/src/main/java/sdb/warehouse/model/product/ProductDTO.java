package sdb.warehouse.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import sdb.warehouse.data.entity.ProductEntity;

public record ProductDTO(
    Integer id,
    String name,
    Integer stockQuantity
) {

  public static ProductDTO fromEntity(ProductEntity entity) {
    return new ProductDTO(
        entity.getExternalProductId(),
        entity.getName(),
        entity.getStockQuantity()
    );
  }
}
