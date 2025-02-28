package sdb.logging.service;

import sdb.logging.model.Log;

import java.util.List;

public interface LogService {

  void log(List<Log> logs);
}
