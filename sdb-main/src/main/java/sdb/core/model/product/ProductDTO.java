package sdb.core.model.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import sdb.core.data.entity.product.ProductEntity;

public record ProductDTO(
    @Schema(description = "ID продукта")
    Integer id,
    @Schema(description = "Название продукта")
    @NotNull(message = "Поле 'productName' не может быть null")
    String productName,
    @Schema(description = "Описание продукта")
    @NotNull(message = "Поле 'description' не может быть null")
    String description,
    @Schema(description = "Цена продукта")
    @NotNull(message = "Поле 'price' не может быть null")
    Integer price
) {
  public static @Nonnull ProductDTO fromEntity(@Nonnull ProductEntity entity) {
    return new ProductDTO(
        entity.getId(),
        entity.getProductName(),
        entity.getDescription(),
        entity.getPrice()
    );
  }

  @Override
  @SneakyThrows
  public String toString() {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(this);
  }
}
