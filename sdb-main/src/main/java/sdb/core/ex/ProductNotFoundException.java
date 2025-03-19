package sdb.core.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product not found")
public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(int id) {
    super("Product with ID " + id + " not found");
  }
}
