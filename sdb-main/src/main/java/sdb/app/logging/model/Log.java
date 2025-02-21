package sdb.app.logging.model;

public record Log(
    String timestamp,
    LogLevel logLevel,
    String path,
    String message
    ) {
}
