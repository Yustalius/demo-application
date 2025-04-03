package sdb.data.entity.products;

import jakarta.annotation.Nonnull;
import lombok.Data;
import sdb.model.product.ProductCoreDTO;

@Data
public class ProductCoreEntity {
  private Integer id;
  private String productName;
  private String description;
  private Integer price;
  private Boolean isAvailable;

  public static @Nonnull ProductCoreEntity fromDTO(@Nonnull ProductCoreDTO product) {
    ProductCoreEntity productEntity = new ProductCoreEntity();
    productEntity.setId(product.id());
    productEntity.setProductName(product.productName());
    productEntity.setDescription(product.description());
    productEntity.setPrice(product.price());
    productEntity.setIsAvailable(product.isAvailable() != null && product.isAvailable());
    return productEntity;
  }
}
