package sdb.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String postgresUrl();

  String coreUrl();

  String loggingUrl();

  String postgresUsername();

  String postgresPassword();
}
