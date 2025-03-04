package sdb.app.model.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.app.data.entity.purchase.PurchaseEntity;

public record PurchaseJson(
    Integer purchaseId,
    Integer userId,
    @JsonProperty("productId")
    Integer productId,
    @JsonProperty("price")
    Integer price,
    Long timestamp
) {
  public static PurchaseJson fromEntity(PurchaseEntity entity) {
    return new PurchaseJson(
        entity.getPurchaseId(),
        entity.getUserId(),
        entity.getProductId(),
        entity.getPrice(),
        entity.getTimestamp()
    );
  }

  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
