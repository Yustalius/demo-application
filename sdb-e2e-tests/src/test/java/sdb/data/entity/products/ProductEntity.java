package sdb.data.entity.products;

import jakarta.annotation.Nonnull;
import lombok.Data;
import sdb.model.product.ProductDTO;

@Data
public class ProductEntity {
  private Integer id;
  private String productName;
  private String description;
  private Integer price;

  public static @Nonnull ProductEntity fromDTO(@Nonnull ProductDTO product) {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId(product.id());
    productEntity.setProductName(product.productName());
    productEntity.setDescription(product.description());
    productEntity.setPrice(product.price());
    return productEntity;
  }
}
