package sdb.data.entity.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.data.entity.products.ProductCoreEntity;

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
  private ProductCoreEntity product;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Integer price;
}
