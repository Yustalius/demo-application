package sdb.core.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.SneakyThrows;
import sdb.core.data.entity.order.OrderEntity;
import sdb.core.model.validation.ValidationGroups.Create;
import sdb.core.model.validation.ValidationGroups.UpdateStatus;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
    @Schema(description = "ID заказа")
    Integer orderId,
    
    @Schema(description = "ID пользователя")
    @NotNull(message = "Поле 'userId' не может быть null", groups = {Default.class, Create.class})
    Integer userId,

    @Schema(description = "Список продуктов")
    @NotNull(message = "Поле 'items' не может быть null", groups = {Default.class, Create.class})
    @NotEmpty(message = "Поле 'items' не может быть пустым", groups = {Default.class, Create.class})
    @Valid
    List<OrderItemDTO> items,

    @Schema(description = "Временная метка")
    String timestamp,

    @Schema(description = "Статус заказа")
    @NotNull(message = "Поле 'status' не может быть null", groups = UpdateStatus.class)
    OrderStatus status,

    @Schema(description = "Причины отмены заказа")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<CancellationReasonDTO> rejectReasons
) {
  public static OrderDTO fromEntity(@Nonnull OrderEntity entity) {
    Integer userId = entity.getUser() != null ? entity.getUser().getId() : null;

    List<OrderItemDTO> orderItems = (entity.getOrderItems() != null) 
        ? entity.getOrderItems().stream()
            .map(item -> new OrderItemDTO(
                (item.getProduct() != null) ? item.getProduct().getId() : null,
                item.getQuantity(),
                item.getPrice()
            ))
            .toList() 
        : Collections.emptyList();

    List<CancellationReasonDTO> cancellationReasons = entity.getCancellationReasons() != null ? 
        entity.getCancellationReasons().stream()
            .map(reason -> {
                JsonNode reasonNode = reason.getReason();
                return new CancellationReasonDTO(
                    reasonNode.get("errorCode") != null ? reasonNode.get("errorCode").asText() : null,
                    reasonNode.has("productId") && !reasonNode.get("productId").isNull() ? reasonNode.get("productId").asInt() : null,
                    reasonNode.has("availableStock") && !reasonNode.get("availableStock").isNull() ? reasonNode.get("availableStock").asInt() : null,
                    reasonNode.has("requestedStock") && !reasonNode.get("requestedStock").isNull() ? reasonNode.get("requestedStock").asInt() : null
                );
            })
            .toList() : null;

    return new OrderDTO(
        entity.getOrderId(),
        userId,
        orderItems,
        entity.getCreatedAt().toString(),
        entity.getStatus(),
        cancellationReasons
    );
  }

  public OrderDTO setStatus(OrderStatus status) {
    return new OrderDTO(
        this.orderId(),
        this.userId,
        this.items,
        this.timestamp,
        status,
        null
    );
  }

  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }
}
