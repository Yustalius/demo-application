package sdb.app.model.order;

public record OrderStatusDTO(
    Integer orderId,
    OrderStatus status
) {
}
