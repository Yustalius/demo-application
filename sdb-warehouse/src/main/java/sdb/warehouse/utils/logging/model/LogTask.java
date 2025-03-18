package sdb.warehouse.utils.logging.model;

public record LogTask(
    String timestamp,
    LogLevel logLevel,
    String path,
    String message
    ) {
}
