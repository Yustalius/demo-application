package sdb.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.data.entity.purchases.PurchaseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
