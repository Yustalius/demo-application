package sdb.app.utils.logging.model;

public record LogTask(
    String timestamp,
    LogLevel logLevel,
    String path,
    String message
    ) {
}
