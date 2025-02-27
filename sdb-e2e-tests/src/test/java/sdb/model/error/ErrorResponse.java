package sdb.model.error;

public record ErrorResponse(
    String errorCode,
    String message
) {
}
