package sdb.core.model.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.SneakyThrows;
import sdb.core.model.validation.ValidationGroups;

import java.util.List;

public record CreateOrderDTO(
    @Schema(description = "ID пользователя")
    @NotNull(message = "ID пользователя не может быть null")
    Integer userId,
    @Schema(description = "Список продуктов")
    @NotNull(message = "Список продуктов не может быть null")
    @NotEmpty(message = "Список продуктов не может быть пустым")
    @Valid
    List<OrderItemDTO> products
) {
  
  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
