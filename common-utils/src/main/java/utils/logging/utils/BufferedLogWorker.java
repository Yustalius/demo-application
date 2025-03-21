package utils.logging.utils;

import utils.logging.api.LogApiClient;
import utils.logging.api.LogWorkerInterface;
import utils.logging.model.LogTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Рабочий поток для обработки логов с буферизацией
 * Собирает логи в буфер и отправляет их пакетами
 */
public class BufferedLogWorker implements LogWorkerInterface {
  private final LogApiClient logClient;

  private static final int BATCH_SIZE = 10;
  private static final int FLUSH_INTERVAL = 1;

  private final List<LogTask> buffer = new ArrayList<>();
  private final ScheduledExecutorService scheduler;

  public BufferedLogWorker(String loggerUrl) {
    this.logClient = new LogApiClient(loggerUrl);
    this.scheduler = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(this::flush, 0, FLUSH_INTERVAL, TimeUnit.SECONDS);
  }

  /**
   * Принудительная отправка всех логов из буфера
   */
  private void flush() {
    synchronized (buffer) {
      if (!buffer.isEmpty()) {
        List<LogTask> batch = new ArrayList<>(buffer);
        buffer.clear();
        try {
          CompletableFuture.runAsync(() -> logClient.sendLog(batch))
              .orTimeout(5, TimeUnit.SECONDS)
              .exceptionally(ex -> {
                System.err.println("Ошибка отправки логов: " + ex.getMessage());
                return null;
              });
        } catch (Exception e) {
          System.err.println("Ошибка при запуске задачи отправки логов: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Добавление лога в буфер
   * @param logTask задание для логирования
   */
  @Override
  public void enqueueLog(LogTask logTask) {
    synchronized (buffer) {
      buffer.add(logTask);

      if (buffer.size() >= BATCH_SIZE) {
        flush();
      }
    }
  }

  /**
   * Остановка рабочего потока
   */
  @Override
  public void shutdown() {
    flush(); // Отправляем все оставшиеся логи
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduler.shutdownNow();
    }
  }
} 