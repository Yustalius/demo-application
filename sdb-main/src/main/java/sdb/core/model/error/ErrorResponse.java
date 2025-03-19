package sdb.core.model.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public record ErrorResponse(
    String errorCode,
    String message
) {
  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
