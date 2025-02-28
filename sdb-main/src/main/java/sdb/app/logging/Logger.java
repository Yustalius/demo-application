package sdb.app.logging;

import sdb.app.logging.api.LogApiClient;
import sdb.app.logging.model.LogLevel;
import sdb.app.logging.model.LogTask;
import sdb.app.logging.utils.LogWorker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static sdb.app.logging.utils.LoggerUtils.getCurrentTimestamp;
import static sdb.app.logging.utils.LoggerUtils.getPath;

public class Logger {
  private final LogWorker logWorker;

  public Logger() {
    this.logWorker = new LogWorker();
  }

  public void info(String message) {
    log(LogLevel.INFO, message);
  }

  public <T> void info(String message, T attachment) {
    log(LogLevel.INFO, message + attachment.toString());
  }

  public void warn(String message) {
    log(LogLevel.WARN, message);
  }

  public <T> void warn(String message, T attachment) {
    log(LogLevel.WARN, message + attachment.toString());
  }

  public void error(String message) {
    log(LogLevel.ERROR, message);
  }

  public <T> void error(String message, T attachment) {
    log(LogLevel.ERROR, message + attachment.toString());
  }

  private void log(LogLevel level, String message) {
    logWorker.enqueueLog(new LogTask(
        getCurrentTimestamp(),
        level,
        getPath(),
        message
    ));
  }
}