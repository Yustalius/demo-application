package sdb.app.logging.utils;

import sdb.app.logging.api.LogApiClient;
import sdb.app.logging.model.LogTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LogWorker {
  private final LogApiClient logClient = new LogApiClient();

  private static final int BATCH_SIZE = 10;
  private static final int FLUSH_INTERVAL = 1;
  private final List<LogTask> buffer = new ArrayList<>();

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public LogWorker() {
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
