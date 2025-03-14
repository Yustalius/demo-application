package sdb.app.ex;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
  }

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(int id) {
    super("Not found user id = %s".formatted(id));
  }
}
