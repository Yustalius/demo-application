package sdb.logging.controller;

import sdb.logging.model.Log;
import sdb.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

  @Autowired
  LogService logger;
  
  // Объект для синхронизации запросов логирования
  private static final Object logMonitor = new Object();

  @PostMapping("/log")
  public void makeLog(@RequestBody List<Log> logEvent) {
    // Синхронизация обработки запросов логирования
    synchronized (logMonitor) {
      logger.log(logEvent);
    }
  }
}
