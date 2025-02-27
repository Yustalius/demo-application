package sdb.app.config;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sdb.app.api.model.error.ErrorResponse;
import sdb.app.ex.UserNotFoundException;
import sdb.app.logging.Logger;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = new Logger();

  @ExceptionHandler(PSQLException.class)
  public ResponseEntity<ErrorResponse> handlePSQLException(PSQLException ex) {
    if (ex.getMessage().contains(") is not present in table \"user_creds\"") || ex.getMessage().contains("Key (user_id)=(")) {
      logger.warn("User not found ", ex.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
              "USER_NOT_FOUND",
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
}