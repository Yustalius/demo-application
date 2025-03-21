package sdb.core.model.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import sdb.core.model.validation.ValidationGroups.Create;

public record OrderItemDTO(
    @Schema(description = "ID продукта")
    @NotNull(message = "ID продукта не может быть null")
    @Min(value = 1, message = "ID продукта должен быть положительным числом")
    Integer productId,

    @Schema(description = "Количество продукта")
    @NotNull(message = "Количество продукта не может быть null")
    @Min(value = 1, message = "Количество продукта должно быть положительным числом")
    Integer quantity,

    @Schema(description = "Цена продукта")
    @NotNull(message = "Цена продукта не может быть null")
    @Min(value = 1, message = "Цена продукта должна быть положительным числом")
    Integer price
) {}
