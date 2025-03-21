package sdb.core.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Order not found")
public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(int id) {
    super("Order with ID " + id + " not found");
  }

  public OrderNotFoundException(String context, int id) {
    super(context + ": " + "Order with ID " + id + " not found");
  }
}
