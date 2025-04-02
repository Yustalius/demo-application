package sdb.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String coreDbUrl();

  String whDbUrl();

  String coreUrl();

  String loggingUrl();

  String whUrl();

  String dbUsername();

  String dbPassword();
}
