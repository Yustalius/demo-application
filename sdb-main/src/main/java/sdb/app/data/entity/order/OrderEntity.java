package sdb.app.data.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.entity.user.UsersEntity;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer purchaseId;

  @ManyToOne
  @JoinColumn(name = "user_id", // <- имя колонки во внешнем ключе
      referencedColumnName = "id", // <- имя колонки в users
      nullable = false)
  private UsersEntity user;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = true)
  private ProductEntity product;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private Long timestamp;
}