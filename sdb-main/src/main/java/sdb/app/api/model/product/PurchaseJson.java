package sdb.app.api.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.app.api.data.entity.product.PurchaseEntity;

public record PurchaseJson(
    Integer purchaseId,
    Integer userId,
    @JsonProperty("product")
    Products productName,
    @JsonProperty("price")
    Integer price,
    Long timestamp
) {
  public static PurchaseJson fromEntity(PurchaseEntity entity) {
    return new PurchaseJson(
        entity.getPurchaseId(),
        entity.getUserId(),
        Products.valueOf(entity.getProduct()),
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
