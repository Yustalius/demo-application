package utils.logging.utils;

import utils.logging.api.LogApiClient;
import utils.logging.api.LogWorkerInterface;
import utils.logging.model.LogTask;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Рабочий поток для обработки логов без буферизации
 * Отправляет каждый лог сразу при получении
 */
public class DirectLogWorker implements LogWorkerInterface {
  private final LogApiClient logClient;

  public DirectLogWorker(String loggerUrl) {
    this.logClient = new LogApiClient(loggerUrl);
  }

  /**
   * Немедленная отправка лога на сервер без буферизации
   * @param logTask задание для логирования
   */
  @Override
  public void enqueueLog(LogTask logTask) {
    try {
      CompletableFuture.runAsync(() -> logClient.sendLog(Collections.singletonList(logTask)))
          .orTimeout(5, TimeUnit.SECONDS)
          .exceptionally(ex -> {
            System.err.println("Ошибка отправки лога: " + ex.getMessage());
            return null;
          });
    } catch (Exception e) {
      System.err.println("Ошибка при запуске задачи отправки лога: " + e.getMessage());
    }
  }

  /**
   * Остановка рабочего потока
   */
  @Override
  public void shutdown() {
    // Для DirectLogWorker нет дополнительных ресурсов для освобождения,
    // так как каждый лог отправляется сразу и нет постоянных потоков
  }
} 