package sdb.core.ex;

public class ProductNotAvailableException extends RuntimeException {
  public ProductNotAvailableException(String message) {
    super(message);
  }

  public ProductNotAvailableException(int id) {
    super("Product with id = " + id + " is not available");
  }
}
