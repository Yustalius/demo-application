package sdb.model.log;

public record LogTask(
    String timestamp,
    LogLevel logLevel,
    String path,
    String message
    ) {
}
