package sdb.core.ex;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException(String message) {
    super(message);
  }

  public InvalidCredentialsException() {
    super("Invalid login or password");
  }
}
