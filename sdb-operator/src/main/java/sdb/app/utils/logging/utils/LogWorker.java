package sdb.app.utils.logging.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.utils.logging.api.LogApiClient;
import sdb.app.utils.logging.model.LogTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LogWorker {
  @Autowired
  private final LogApiClient logClient;

  private static final int BATCH_SIZE = 10;
  private static final int FLUSH_INTERVAL = 1;

  private final List<LogTask> buffer = new ArrayList<>();
  private final ScheduledExecutorService scheduler;

  @Autowired
  public LogWorker(LogApiClient logClient) {
    this.logClient = logClient;
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
