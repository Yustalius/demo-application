package sdb.logging.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sdb.logging.config.Config;
import sdb.logging.model.Log;
import sdb.logging.model.LogTag;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
  private static final Config CFG = Config.getInstance();
  private static final String LOG_FORMAT = "[%s] [%s] [%s] - %s";
  private static final int BUFFER_SIZE = 1000;
  private static final long FLUSH_INTERVAL_MS = 5000;
  private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

  // Используем LinkedList вместо ArrayList для сохранения порядка вставки
  private final Map<String, LinkedList<Log>> logBuffers = new ConcurrentHashMap<>();
  private final Map<String, ReentrantLock> fileLocks = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public LogServiceImpl() {
    Arrays.stream(LogTag.values())
        .forEach(tag -> {
            logBuffers.put(tag.getLogFilePath(), new LinkedList<>());
            fileLocks.put(tag.getLogFilePath(), new ReentrantLock());
        });

    scheduler.scheduleAtFixedRate(this::flushAllBuffers, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);
  }

  @Override
  public void log(List<Log> logs) {
    if (logs == null || logs.isEmpty()) {
      return;
    }

    Map<Log, Set<LogTag>> logsMap = new LinkedHashMap<>();

    for (Log log : logs) {
      Set<LogTag> tags = new HashSet<>();
      tags.add(LogTag.ALL);

      if (log.path().startsWith("s.w.")) {
        tags.add(LogTag.WAREHOUSE);
      }
      if (log.path().startsWith("s.c.")) {
        tags.add(LogTag.CORE);
      }
      if (log.path().contains("Rabbit") || log.path().contains("Event")) {
        tags.add(LogTag.RABBIT);
      }

      logsMap.put(log, tags);
    }

    processLogs(logsMap);
  }

  private void processLogs(Map<Log, Set<LogTag>> logsMap) {
    logsMap.forEach((log, tags) -> {
      logToSlf4j(log);

      tags.forEach(tag -> {
        String filePath = tag.getLogFilePath();
        ReentrantLock lock = fileLocks.get(filePath);
        LinkedList<Log> buffer = logBuffers.get(filePath);
        
        lock.lock();
        try {
          buffer.addLast(log);
          
          // Если буфер заполнен, записываем его
          if (buffer.size() >= BUFFER_SIZE) {
            flushBuffer(filePath);
          }
        } finally {
          lock.unlock();
        }
      });
    });
  }

  private String formatLog(Log log) {
    return String.format(LOG_FORMAT,
        log.timestamp(),
        log.logLevel(),
        log.path(),
        log.message());
  }

  private void flushAllBuffers() {
    logBuffers.keySet().forEach(this::flushBuffer);
  }

  private void flushBuffer(String filePath) {
    ReentrantLock lock = fileLocks.get(filePath);
    if (!lock.tryLock()) {
      return;
    }
    
    try {
      LinkedList<Log> buffer = logBuffers.get(filePath);
      if (buffer.isEmpty()) {
        return;
      }
      
      List<Log> logsToDump = new ArrayList<>(buffer);
      buffer.clear();
      
      writeLogsToFile(filePath, logsToDump);
    } finally {
      lock.unlock();
    }
  }

  private void writeLogsToFile(String filePath, List<Log> logs) {
    if (logs.isEmpty()) {
      return;
    }
    
    Path path = Paths.get(filePath);
    try {
      Files.createDirectories(path.getParent());
      if (!Files.exists(path)) {
        Files.createFile(path);
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
        for (Log log : logs) {
          writer.write(formatLog(log));
          writer.newLine();
        }
        writer.flush(); // Гарантируем запись на диск
      }
    } catch (IOException e) {
      log.error("Ошибка при записи логов в файл {}: {}", filePath, e.getMessage());
    }
  }

  private void logToSlf4j(Log log) {
    switch (log.logLevel()) {
      case INFO -> logger.info(log.message());
      case WARN -> logger.warn(log.message());
      case ERROR -> logger.error(log.message());
    }
  }

  // Метод для принудительной записи всех буферов при завершении работы
  public void shutdown() {
    flushAllBuffers();
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduler.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}