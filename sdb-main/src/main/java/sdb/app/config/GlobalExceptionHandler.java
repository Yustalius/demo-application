package sdb.app.config;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sdb.app.ex.OrderNotFoundException;
import sdb.app.ex.ProductNotFoundException;
import sdb.app.model.error.ErrorResponse;
import sdb.app.ex.UserNotFoundException;
import sdb.app.logging.Logger;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @Autowired
  private Logger logger;

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    if (ex.getMessage().contains(") is not present in table \"user_creds\"") || ex.getMessage().contains("Key (user_id)=(")) {
      logger.warn("User not found ", ex.getMessage());
      return ResponseEntity.status(NOT_FOUND)
          .body(new ErrorResponse(
              "USER_NOT_FOUND",
              ex.getMessage()
          ));
    } else if (ex.getMessage().contains("constraint [user_creds_unique]")) {
      logger.warn("Username already exists ", ex.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
              "USERNAME_ALREADY_TAKEN",
              ex.getMessage()
          ));
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(
            "UNKNOWN",
            ex.getMessage()
            ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            "USER_NOT_FOUND",
            ex.getMessage()
        ));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            "PRODUCT_NOT_FOUND",
            ex.getMessage()
        ));
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            "ORDER_NOT_FOUND",
            ex.getMessage()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(new ErrorResponse(
            "BAD_REQUEST",
            ex.getMessage()
        ));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    if (ex.getMessage().contains(") is not present in table \"user_creds\"") || ex.getMessage().contains("Key (user_id)=(")) {
      logger.warn("User not found ", ex.getMessage());
      return ResponseEntity.status(NOT_FOUND)
          .body(new ErrorResponse(
              "USER_NOT_FOUND",
              ex.getMessage()
          ));
    }

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(
            "UNKNOWN",
            ex.getMessage()
        ));
  }
}