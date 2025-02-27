package sdb.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String postgresUrl();

  String userApiUrl();

  String postgresUsername();

  String postgresPassword();
}
