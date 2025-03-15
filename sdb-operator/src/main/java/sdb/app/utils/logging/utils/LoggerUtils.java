package sdb.app.utils.logging.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtils {

  public static String getCurrentTimestamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }

  public static String getPath() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length > 4) {

      return abbreviatePackageNames(stackTrace[4].getClassName()) +
          stackTrace[4].getMethodName() + ":" +
          stackTrace[4].getLineNumber();
    }
    return "unknown";
  }

  private static String abbreviatePackageNames(String stackTrace) {
    String[] parts = stackTrace.split("\\.");
    if (parts.length <= 1) return stackTrace;

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length - 1; i++) {
      result.append(parts[i].charAt(0)).append(".");
    }

    for (int i = parts.length - 1;  i < parts.length; i++) {
      result.append(parts[i]).append(".");
    }

    return result.toString();
  }
}