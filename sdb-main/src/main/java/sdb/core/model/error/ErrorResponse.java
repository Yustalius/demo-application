package sdb.core.model.error;

public record ErrorResponse(
    String errorCode,
    String message
) {
}
