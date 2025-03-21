package sdb.core.data.entity.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sdb.core.data.entity.user.UsersEntity;
import sdb.core.model.order.OrderStatus;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orderId;

  @ManyToOne
  @JoinColumn(name = "user_id",
      referencedColumnName = "id", // <- имя колонки в user_creds
      nullable = false)
  private UsersEntity user;

  @Column(name = "status", columnDefinition = "varchar(20) check (status in ('PENDING', 'APPROVED', 'REJECTED', 'IN_WORK', 'FINISHED', 'CANCELED'))")
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