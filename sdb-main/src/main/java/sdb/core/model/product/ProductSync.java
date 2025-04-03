package sdb.core.model.product;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSync(
    @Schema(description = "Количество обновленных продуктов")
    int updatedProducts,
    
    @Schema(description = "Количество добавленных продуктов")
    int addedProducts
) {
}
