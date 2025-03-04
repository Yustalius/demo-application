package sdb.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.model.product.ProductDTO;
import sdb.app.service.impl.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping("/add")
  public ProductDTO createProduct(@RequestBody ProductDTO product) {
    return productService.create(product);
  }

  @PatchMapping("{id}")
  public ProductDTO updateProduct(
      @PathVariable int id,
      @RequestBody ProductDTO product) {
    return productService.update(id, product);
  }

  @GetMapping("{id}")
  public ProductEntity getProductById(@PathVariable int id) {
    return productService.getById(id);
  }

  @GetMapping
  public List<ProductEntity> getAllProducts() {
    return productService.getAll();
  }

  @DeleteMapping("{id}")
  public void deleteProduct(@PathVariable int id) {
    productService.delete(id);
  }
}
