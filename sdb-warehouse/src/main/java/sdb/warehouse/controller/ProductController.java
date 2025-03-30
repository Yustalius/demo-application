package sdb.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.warehouse.model.product.ProductDTO;
import sdb.warehouse.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public List<ProductDTO> getProducts() {
    return productService.getAll().stream()
        .filter(product -> product.stockQuantity() > 0)
        .toList();
  }
}
