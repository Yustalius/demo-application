package sdb.logging.service;

import sdb.logging.model.Log;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class LogServiceImpl implements LogService {

  private static final String LOG_FILE_PATH = "sdb-logging/log/sdb.log";

  @Override
  public synchronized void log(Log log) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
      writer.write(String.format("%s %s %s - %s",
          log.timestamp(),
          log.logLevel(),
          log.path(),
          log.message()));
      writer.newLine();
    } catch (IOException e) {
      System.err.println("Failed creating log " + e.getMessage());
    }
  }
}
