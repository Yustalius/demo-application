package com.example.demo.utils.logging;

import com.example.demo.utils.logging.api.LogApiClient;
import com.example.demo.utils.logging.model.LogLevel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

  LogApiClient logClient = new LogApiClient();

  public void info(String message) {
    logClient.sendLog(
        getCurrentTimestamp(),
        LogLevel.INFO,
        getCallingClassName(),
        message
        );
  }

  private static String getCurrentTimestamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }

  private static String getCallingClassName() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length > 3) {
      return abbreviatePackageNames(stackTrace[3].toString());
    }
    return "unknown";
  }

  private static String abbreviatePackageNames(String stackTrace) {
    String[] parts = stackTrace.split("\\.");
    String[] split = parts[parts.length - 2].split("\\(");
    parts[parts.length - 2] = split[0] + "()";
    if (parts.length <= 1) return stackTrace;

    for (int i = 0; i < parts.length - 4; i++) {
      parts[i] = String.valueOf(parts[i].charAt(0));
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length - 1; i++) {
      result.append(parts[i]).append(".");
    }

    result.deleteCharAt(result.length() - 1);
    return result.toString();
  }
}