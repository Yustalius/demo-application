package sdb.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdb.core.model.product.CreateProductDTO;
import sdb.core.model.product.ProductDTO;
import sdb.core.service.ProductService;
import sdb.core.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import utils.logging.Logger;

import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "Продукты", description = "Операции с продуктами")
public class ProductController {

  @Autowired
  private Logger logger;

  @Autowired
  private ProductService productService;

  @Operation(summary = "Добавление продукта", description = "Добавляет новый продукт в базу данных")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукт успешно добавлен",
          content = @Content(schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("/add")
  public ProductDTO add(@Validated @RequestBody CreateProductDTO product) {
    logger.info("Creating product " + product);
    return productService.create(product);
  }

  @Operation(summary = "Обновление продукта", description = "Частичное или полное обновление продукта")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен",
          content = @Content(schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "ProductNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PatchMapping("{id}")
  public ProductDTO update(
      @PathVariable int id,
      @RequestBody CreateProductDTO product) {
    return productService.update(id, product);
  }

  @Operation(summary = "Получение продукта по ID", description = "Возвращает продукт, соответствующий указанному ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукт успешно получен",
          content = @Content(schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "ProductNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping("{id}")
  public ProductDTO byId(@PathVariable int id) {
    logger.info("Getting product with id " + id);
    return productService.getById(id);
  }

  @Operation(summary = "Получение всех продуктов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукты успешно получены",
          content = @Content(schema = @Schema(implementation = ProductDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping
  public List<ProductDTO> get() {
    logger.info("Getting all items");
    return productService.getAll();
  }

  @Operation(summary = "Удаление продукта по ID",
      description = "Удаляет продукт по его ID, при этом в связанных заказах продукт становится null")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукт успешно удален"),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "ProductNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @DeleteMapping("{id}")
  public void delete(@PathVariable int id) {
    logger.info("Deleting product with id " + id);
    productService.delete(id);
  }

  @Operation(summary = "Синхронизация продуктов", description = "Синхронизирует продукты с базой данных")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Продукты успешно синхронизированы"),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("/sync")
  public void syncProducts() {
    productService.sync();
  }
}
