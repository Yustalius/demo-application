package sdb.warehouse.ex;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(int id) {
    super("Order with ID " + id + " not found");
  }

  public OrderNotFoundException(String context, int id) {
    super(context + ": " + "Order with ID " + id + " not found");
  }
}
