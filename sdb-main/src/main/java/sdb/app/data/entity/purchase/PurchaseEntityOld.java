package sdb.app.data.entity.purchase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.app.model.purchase.OrderDTO;

@Getter
@Setter
@ToString
public class PurchaseEntityOld {
  private Integer purchaseId;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private Long timestamp;

  public static PurchaseEntityOld fromJson(OrderDTO json) {
    PurchaseEntityOld entity = new PurchaseEntityOld();
    entity.setUserId(json.userId());
    entity.setProductId(json.productId());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());

    return entity;
  }
}
