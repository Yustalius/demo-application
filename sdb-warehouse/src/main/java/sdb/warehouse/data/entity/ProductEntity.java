package sdb.warehouse.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import sdb.warehouse.model.product.ProductDTO;

/**
 * Сущность товара в системе склада.
 * Соответствует таблице products в базе данных.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

  /**
   * Уникальный идентификатор товара в системе склада
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Внешний идентификатор товара (из основной системы)
   */
  @Column(name = "external_product_id", nullable = false, unique = true)
  private Integer externalProductId;

  /**
   * Название продукта (может отличаться от названия в основной системе)
   */
  @Column(nullable = false)
  private String name;

  /**
   * Количество товара на складе
   */
  @Column(name = "stock_quantity", nullable = false)
  private Integer stockQuantity;

  public static ProductEntity fromDTO(ProductDTO productDTO) {
    return ProductEntity.builder()
        .id(productDTO.id())
        .externalProductId(productDTO.externalProductId())
        .name(productDTO.name())
        .stockQuantity(productDTO.stockQuantity())
        .build();
  }
} 