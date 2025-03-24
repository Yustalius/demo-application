package utils.ex;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(int id) {
    super("Product with ID " + id + " not found");
  }

  public ProductNotFoundException(String message) {
    super(message);
  }

  public ProductNotFoundException(String context, int id) {
    super(context + ": " + "Product with ID " + id + " not found");
  }
}
