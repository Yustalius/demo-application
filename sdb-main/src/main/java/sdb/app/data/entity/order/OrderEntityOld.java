package sdb.app.data.entity.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.app.model.order.OrderDTO;

@Getter
@Setter
@ToString
public class OrderEntityOld {
  private Integer purchaseId;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private Long timestamp;

  public static OrderEntityOld fromJson(OrderDTO json) {
    OrderEntityOld entity = new OrderEntityOld();
    entity.setUserId(json.userId());
    entity.setProductId(json.productId());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());

    return entity;
  }
}
