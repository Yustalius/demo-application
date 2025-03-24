package sdb.core.model.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import sdb.core.model.validation.ValidationGroups.Create;

public record OrderItemDTO(
    @Schema(description = "ID продукта")
    @NotNull(message = "Поле 'productId' не может быть null")
    @Min(value = 1, message = "Поле 'productId' должно быть положительным числом")
    Integer productId,

    @Schema(description = "Количество продукта")
    @NotNull(message = "Поле 'quantity' не может быть null")
    @Min(value = 1, message = "Поле 'quantity' должно быть положительным числом")
    Integer quantity,

    @Schema(description = "Цена продукта")
    @NotNull(message = "Поле 'price' не может быть null")
    @Min(value = 1, message = "Поле 'price' должно быть положительным числом")
    Integer price
) {}
