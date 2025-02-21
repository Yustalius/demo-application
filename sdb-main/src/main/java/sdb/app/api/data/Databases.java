package sdb.app.api.data;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
  private Databases() {
  }

  private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      T result = function.apply(connection);
      connection.commit();
      connection.setAutoCommit(true);
      return result;
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }

      throw new RuntimeException(e);
    }
  }

  public static void transaction(Consumer<Connection> consumer, String jdbcUrl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      consumer.accept(connection);
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }

      throw new RuntimeException(e);
    }
  }

  private static DataSource dataSource(String jdbcUrl) {
    return datasources.computeIfAbsent(
        jdbcUrl,
        key -> {
          PGSimpleDataSource ds = new PGSimpleDataSource();
          ds.setUser("postgres");
          ds.setPassword("1234");
          ds.setUrl(key);
          return ds;
        }
    );
  }

  public static Connection connection(String jdbcUrl) throws SQLException {
    return dataSource(jdbcUrl).getConnection();
  }
}
