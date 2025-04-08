package sdb.core.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CancellationReasonDTO(
  String errorCode,
  Integer productId,
  Integer availableStock,
  Integer requestedStock
) {
}
