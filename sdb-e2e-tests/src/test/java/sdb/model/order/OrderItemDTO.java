package sdb.model.order;

public record OrderItemDTO(
    Integer productId,
    Integer price,
    Integer quantity
) {}
