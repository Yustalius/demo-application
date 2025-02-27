package sdb.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

enum LocalConfig implements Config {
  INSTANCE;

  Map<String, Object> data;
  Map<String, String> sqlConfig;
  Map<String, String> apiConfig;

  LocalConfig() {
    Yaml yaml = new Yaml();
    InputStream inputStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream("config.yaml");

    data = yaml.load(inputStream);
    sqlConfig = (Map<String, String>) data.get("sql");
    apiConfig = (Map<String, String>) data.get("api");
  }

  @Override
  public String postgresUrl() {
    return sqlConfig.get("postgres-url");
  }

  @Override
  public String userApiUrl() {
    return apiConfig.get("user-api-url");
  }

  @Override
  public String postgresUsername() {
    return sqlConfig.get("postgres-username");
  }

  @Override
  public String postgresPassword() {
    return sqlConfig.get("postgres-password");
  }
}