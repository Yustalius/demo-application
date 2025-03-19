package sdb.logging.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String logFilePath();

  String coreLogFilePath();

  String whLogFilePath();

  String rabbitLogFilePath();
}
