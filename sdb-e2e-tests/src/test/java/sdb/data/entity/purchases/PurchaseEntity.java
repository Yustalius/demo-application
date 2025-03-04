package sdb.data.entity.purchases;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.model.product.PurchaseJson;

@Getter
@Setter
@ToString
public class PurchaseEntity {
  private Integer purchaseId;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private Long timestamp;

  public static PurchaseEntity fromJson(PurchaseJson json) {
    PurchaseEntity entity = new PurchaseEntity();
    entity.setUserId(json.userId());
    entity.setProductId(json.productId());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());

    return entity;
  }
}
