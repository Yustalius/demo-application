package sdb.app.model.error;

public record ErrorResponse(
    String errorCode,
    String message
) {
}
