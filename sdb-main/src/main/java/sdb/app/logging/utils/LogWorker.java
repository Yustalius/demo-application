package sdb.app.logging.utils;

import sdb.app.logging.api.LogApiClient;
import sdb.app.logging.model.LogTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class LogWorker {
  private final LogApiClient logClient;
  private final ExecutorService executor;
  private final LinkedBlockingQueue<LogTask> logQueue;

  public LogWorker() {
    this.logClient = new LogApiClient();
    this.executor = Executors.newFixedThreadPool(2);
    this.logQueue = new LinkedBlockingQueue<>();
    startBackgroundWorker();
  }

  private void startBackgroundWorker() {
    executor.submit(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          logClient.sendLog(logQueue.take());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
  }

  public void enqueueLog(LogTask logTask) {
    logQueue.add(logTask);
  }
}
