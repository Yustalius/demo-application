package sdb.data.entity.products;

import jakarta.annotation.Nonnull;
import lombok.Data;
import sdb.model.product.ProductWhDTO;

@Data
public class ProductWhEntity {
  private Integer id;
  private Integer externalProductId;
  private String name;
  private Integer stockQuantity;

  public static @Nonnull ProductWhEntity fromDTO(@Nonnull ProductWhDTO product) {
    ProductWhEntity productEntity = new ProductWhEntity();
    productEntity.setId(product.id());
    productEntity.setExternalProductId(product.externalProductId());
    productEntity.setName(product.name());
    productEntity.setStockQuantity(product.stockQuantity());
    return productEntity;
  }
}
