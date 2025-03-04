package sdb.app.data.entity.product;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.app.model.product.ProductDTO;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column
  private String description;

  @Column(nullable = false)
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
