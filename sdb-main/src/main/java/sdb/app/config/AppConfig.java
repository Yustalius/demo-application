package sdb.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sdb.app.api.data.Databases;

import java.sql.Connection;

@Configuration
public class AppConfig {
  private static final Config CFG = Config.getInstance();

  @Bean(name = "dbConnection")
  public Connection connection() {
    return Databases.connection(CFG.postgresUrl());
  }
}
