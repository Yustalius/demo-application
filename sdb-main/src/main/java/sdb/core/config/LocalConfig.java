package sdb.core.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

enum LocalConfig implements Config {
    INSTANCE;

  Map<String, Object> data;
  Map<String, String> sqlConfig;
  Map<String, String> appConfig;

  LocalConfig() {
    Yaml yaml = new Yaml();
    InputStream inputStream = getClass()
        .getClassLoader()
        .getResourceAsStream("config.yaml");

    data = yaml.load(inputStream);
    appConfig = (Map<String, String>) data.get("api");
  }

  @Override
  public String postgresUrl() {
    return sqlConfig.get("postgres-url");
  }

  @Override
  public String postgresUsername() {
    return sqlConfig.get("postgres-username");
  }

  @Override
  public String postgresPassword() {
    return sqlConfig.get("postgres-password");
  }

  @Override
  public String loggerUrl() {
    return appConfig.get("logging.url");
  }
}