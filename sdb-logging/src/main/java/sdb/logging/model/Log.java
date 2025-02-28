package sdb.logging.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public record Log(
    String timestamp,
    LogLevel logLevel,
    String path,
    String message
    ) {

    @Override
    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
