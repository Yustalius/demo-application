package sdb.core.data.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.core.data.entity.product.ProductEntity;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Integer orderItemId;

  @ManyToOne
  @JoinColumn(name = "order_id",
      referencedColumnName = "orderId",
      nullable = false)
  private OrderEntity order;

  @ManyToOne
  @JoinColumn(name = "product_id",
      referencedColumnName = "id",
      nullable = true)
  private ProductEntity product;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Integer price;
}
