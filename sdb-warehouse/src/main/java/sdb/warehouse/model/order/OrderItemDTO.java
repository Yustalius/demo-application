package sdb.warehouse.model.order;

public record OrderItemDTO(
    Integer productId,
    Integer quantity,
    Integer price
) {}
