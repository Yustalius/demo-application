package utils.logging.utils;


import utils.logging.api.LogApiClient;
import utils.logging.model.LogTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogWorker {
  private final LogApiClient logClient;

  private static final int BATCH_SIZE = 10;
  private static final int FLUSH_INTERVAL = 1;

  private final List<LogTask> buffer = new ArrayList<>();
  private final ScheduledExecutorService scheduler;

  public LogWorker(String loggerUrl) {
    this.logClient = new LogApiClient(loggerUrl);
    this.scheduler = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(this::flush, 0, FLUSH_INTERVAL, TimeUnit.SECONDS);
  }

  private void flush() {
    synchronized (buffer) {
      if (!buffer.isEmpty()) {
        List<LogTask> batch = new ArrayList<>(buffer);
        buffer.clear();
        logClient.sendLog(batch);
      }
    }
  }

  public void enqueueLog(LogTask logTask) {
    synchronized (buffer) {
      buffer.add(logTask);

      if (buffer.size() >= BATCH_SIZE) {
        flush();
      }
    }
  }

  public void shutdown() {
    flush();
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
