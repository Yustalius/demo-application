package sdb.app.api.data.entity.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sdb.app.api.model.product.PurchaseJson;

@Getter
@Setter
@ToString
public class PurchaseEntity {
  private Integer purchaseId;
  private Integer userId;
  private String product;
  private Integer price;
  private Long timestamp;

  public static PurchaseEntity fromJson(PurchaseJson json) {
    PurchaseEntity entity = new PurchaseEntity();
    entity.setUserId(json.userId());
    entity.setProduct(json.productName().toString());
    entity.setPrice(json.price());
    entity.setTimestamp(json.timestamp());

    return entity;
  }
}
