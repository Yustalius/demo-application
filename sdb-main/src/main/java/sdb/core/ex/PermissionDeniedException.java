package sdb.core.ex;

public class PermissionDeniedException extends RuntimeException {
  public PermissionDeniedException(String message) {
    super(message);
  }

  public PermissionDeniedException() {
    super("Нет прав на выполнение операции");
  }
}
