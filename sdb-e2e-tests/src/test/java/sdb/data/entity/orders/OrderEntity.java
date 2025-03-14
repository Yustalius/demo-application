package sdb.data.entity.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.model.product.OrderDTO;

@Getter
@Setter
@ToString
public class OrderEntity {
  private Integer purchaseId;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private Long timestamp;

  public static OrderEntity fromJson(OrderDTO json) {
    OrderEntity entity = new OrderEntity();
    entity.setUserId(json.userId());
    entity.setProductId(json.productId());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());

    return entity;
  }
}
