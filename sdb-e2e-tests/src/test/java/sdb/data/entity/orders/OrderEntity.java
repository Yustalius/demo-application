package sdb.data.entity.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderStatus;

@Getter
@Setter
@ToString
public class OrderEntity {
  private Integer purchaseId;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private Long timestamp;
  private OrderStatus status;

  public static OrderEntity fromJson(OrderDTO json) {
    OrderEntity entity = new OrderEntity();
    entity.setUserId(json.userId());
    entity.setProductId(json.productId());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());
    entity.setStatus(json.status());

    return entity;
  }
}
