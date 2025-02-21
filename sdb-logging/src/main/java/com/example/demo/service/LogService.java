package com.example.demo.service;

import com.example.demo.model.Log;

public class LogService {

  public void log(Log log) {
    System.out.println(createLogMessage(log));
  }

  private static String createLogMessage(Log log) {
    return String.format("%s %s %s - %s",
        log.timestamp(),
        log.logLevel(),
        log.path(),
        log.message());
  }

}
