package sdb.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sdb.core.data.Databases;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class AppConfig {
  private static final Config CFG = Config.getInstance();

  @Value("${app.database.url}")
  private String dbUrl;

  @Bean(name = "dbConnection")
  public Connection connection() {
    return Databases.connection(dbUrl);
  }

  @Bean(name = "dbDatasource")
  public DataSource dataSource() {
    return Databases.dataSource(dbUrl);
  }
}
