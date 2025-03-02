package sdb.logging.service;

import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sdb.logging.config.Config;
import sdb.logging.model.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
  private static final Config CFG = Config.getInstance();
  private static final String SAMPLE_LOG = "[%s] [%s] [%s] - %s";

  private final Logger logger = LoggerFactory.getLogger(LogServiceImpl .class);

  @Synchronized
  @Override
  public void log(List<Log> logs) {
    Path path = Paths.get(CFG.logFilePath());

    try {
      if (!Files.exists(path)) {
        Files.createDirectories(path.getParent());
        Files.createFile(path);
      }
    } catch (IOException e) {
      System.err.println("Failed to create log file: " + e.getMessage());
      return;
    }

    for (Log log : logs) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(CFG.logFilePath(), true))) {
        String formattedLog = String.format(SAMPLE_LOG,
            log.timestamp(),
            log.logLevel(),
            log.path(),
            log.message());

        switch (log.logLevel()) {
          case INFO -> logger.info(log.message());
          case WARN -> logger.warn(log.message());
          case ERROR -> logger.error(log.message());
        }

        writer.write(formattedLog);
        writer.newLine();
      } catch (IOException e) {
        System.err.println("Failed writing log: " + e.getMessage());
      }
    }
  }
}