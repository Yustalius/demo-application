package sdb.core.model.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import sdb.core.model.order.ErrorCode;

public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {
  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
