package sdb.logging.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String postgresUrl();

  String postgresUsername();

  String postgresPassword();
}
