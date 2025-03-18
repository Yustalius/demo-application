package utils.logging;


import utils.logging.model.LogLevel;
import utils.logging.model.LogTask;
import utils.logging.utils.LogWorker;

import static utils.logging.utils.LoggerUtils.getCurrentTimestamp;
import static utils.logging.utils.LoggerUtils.getPath;

public class Logger {
  private final LogWorker logWorker;

  public Logger(String loggerUrl) {
    this.logWorker = new LogWorker(loggerUrl);
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