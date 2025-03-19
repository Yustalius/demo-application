package sdb.core.model.order;

public record OrderStatusDTO(
    Integer orderId,
    OrderStatus status
) {
}
