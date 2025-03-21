package sdb.core.config;

import static org.springframework.http.HttpStatus.*;
import static sdb.core.model.order.ErrorCode.*;
import static sdb.core.model.order.ErrorCode.BAD_REQUEST;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sdb.core.ex.*;
import sdb.core.model.error.ErrorResponse;
import utils.logging.Logger;

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
              USER_NOT_FOUND,
              ex.getMessage()
          ));
    } else if (ex.getMessage().contains("constraint [user_creds_unique]")) {
      logger.info("Username already exists ", ex.getMessage());
      return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(
              USERNAME_ALREADY_TAKEN,
              ex.getMessage()
          ));
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(
            DATABASE_ERROR,
            ex.getMessage()
            ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    logger.warn("User not found ", ex.getMessage());
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            USER_NOT_FOUND,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
    logger.warn(ex.getMessage());
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            PRODUCT_NOT_FOUND,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
    logger.warn(ex.getMessage());
    return ResponseEntity.status(NOT_FOUND)
        .body(new ErrorResponse(
            ORDER_NOT_FOUND,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
    logger.info("Invalid credentials ", ex.getMessage());
    return ResponseEntity.status(UNAUTHORIZED)
        .body(new ErrorResponse(
            INVALID_CREDS,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(PermissionDeniedException.class)
  public ResponseEntity<ErrorResponse> handlePermissionDeniedException(PermissionDeniedException ex) {
    logger.warn("Permission denied ", ex.getMessage());
    return ResponseEntity.status(FORBIDDEN)
        .body(new ErrorResponse(
            PERMISSION_DENIED,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(StatusTransitionException.class)
  public ResponseEntity<ErrorResponse> handleOrderStatusTransitionException(StatusTransitionException ex) {
    logger.warn("Transition from " + ex.getCurrentStatus() + " to " + ex.getNewStatus() + " is not allowed ", ex.getMessage());
    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
            STATUS_TRANSITION_ERROR,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
            BAD_REQUEST,
            errorMessage
        ));
  }
  
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    if (ex.getMessage().contains(") is not present in table \"user_creds\"") || ex.getMessage().contains("Key (user_id)=(")) {
      logger.warn("User not found ", ex.getMessage());
      return ResponseEntity.status(NOT_FOUND)
          .body(new ErrorResponse(
              USER_NOT_FOUND,
              ex.getMessage()
          ));
    }

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(
            UNKNOWN,
            ex.getMessage()
        ));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    String errorMessage = "Ошибка при чтении JSON";

    // Проверяем, является ли причиной ошибки неверный формат enum
    if (ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException invalidFormatEx = (InvalidFormatException) ex.getCause();

      // Проверяем, что это ошибка с enum
      if (invalidFormatEx.getTargetType() != null && invalidFormatEx.getTargetType().isEnum()) {
        String fieldName = invalidFormatEx.getPath().isEmpty() ? "поле" :
            invalidFormatEx.getPath().get(invalidFormatEx.getPath().size() - 1).getFieldName();

        String invalidValue = invalidFormatEx.getValue().toString();

        // Получаем все допустимые значения enum
        Object[] enumValues = invalidFormatEx.getTargetType().getEnumConstants();
        String validValues = Arrays.stream(enumValues)
            .map(Object::toString)
            .collect(Collectors.joining(", "));

        errorMessage = String.format(
            "Поле '%s' со значением '%s' должно быть одним из: [%s]",
            fieldName,
            invalidValue,
            validValues
        );
      }
    }

    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
            BAD_REQUEST,
            errorMessage
        ));
  }
}