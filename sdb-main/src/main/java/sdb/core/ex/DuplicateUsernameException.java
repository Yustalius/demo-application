package sdb.core.ex;

public class DuplicateUsernameException extends RuntimeException {
  public DuplicateUsernameException(String message) {
    super(message);
  }
}
