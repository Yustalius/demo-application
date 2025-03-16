package sdb.app.data.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.model.order.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders3")
@Getter
@Setter
public class OrderEntity3 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orderId;

  @ManyToOne
  @JoinColumn(name = "user_id",
      referencedColumnName = "id", // <- имя колонки в users
      nullable = false)
  private UsersEntity user;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemEntity> orderItems = new ArrayList<>();

  public void addOrderItem(OrderItemEntity item) {
    orderItems.add(item);
    item.setOrder(this);
  }

  public void removeOrderItem(OrderItemEntity item) {
    orderItems.remove(item);
    item.setOrder(null);
  }
}