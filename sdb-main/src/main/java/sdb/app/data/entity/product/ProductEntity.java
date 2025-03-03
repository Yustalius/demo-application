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

  public static @Nonnull ProductEntity fromDTO(@Nonnull ProductDTO dto) {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId(dto.id());
    productEntity.setProductName(dto.productName());
    productEntity.setDescription(dto.description());
    return productEntity;
  }
}
