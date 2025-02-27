package sdb.app.api.model.error;

public record ErrorResponse(
    String errorCode,
    String message
) {
}
