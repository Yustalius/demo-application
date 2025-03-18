package sdb.app.model.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import sdb.app.model.validation.ValidationGroups.Create;

public record OrderItemDTO(
    @NotNull(message = "ID продукта не может быть null", groups = {Default.class, Create.class})
    @Min(value = 1, message = "ID продукта должен быть положительным числом", groups = {Default.class, Create.class})
    Integer productId,

    @NotNull(message = "Количество продукта не может быть null", groups = {Default.class, Create.class})
    @Min(value = 1, message = "Количество продукта должно быть положительным числом", groups = {Default.class, Create.class})
    Integer quantity,

    @NotNull(message = "Цена продукта не может быть null", groups = {Default.class, Create.class})
    @Min(value = 1, message = "Цена продукта должна быть положительным числом", groups = {Default.class, Create.class})
    Integer price
) {}
