package sdb.app.logging;

import sdb.app.logging.api.LogApiClient;
import sdb.app.logging.model.LogLevel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
  LogApiClient logClient;

  public Logger() {
    this.logClient = new LogApiClient();
  }

  public void info(String message) {
    logClient.sendLog(
        getCurrentTimestamp(),
        LogLevel.INFO,
        getPath(),
        message);
  }

  private static String getCurrentTimestamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }

  private static String getPath() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length > 3) {

      return abbreviatePackageNames(stackTrace[3].getClassName()) +
          stackTrace[3].getMethodName() + ":" +
          stackTrace[3].getLineNumber();
    }
    return "unknown";
  }

  private static String abbreviatePackageNames(String stackTrace) {
    String[] parts = stackTrace.split("\\.");
    if (parts.length <= 1) return stackTrace;

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length - 2; i++) {
      result.append(parts[i].charAt(0)).append(".");
    }

    for (int i = parts.length - 2;  i < parts.length; i++) {
      result.append(parts[i]).append(".");
    }

    return result.toString();
  }
}