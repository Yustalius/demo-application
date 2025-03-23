package sdb.core.model.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import sdb.core.model.validation.ValidationGroups;

public record StatusDTO(
    @Schema(description = "Статус заказа")
    @NotNull(message = "Поле 'status' не может быть null")
    OrderStatus status
) {
}
