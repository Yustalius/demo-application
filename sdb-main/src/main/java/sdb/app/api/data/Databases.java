package sdb.app.api.data;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

  public static <T> T transaction(Supplier<T> supplier, Connection conn) {
    try {
      conn.setAutoCommit(false);
      T result = supplier.get();
      conn.commit();
      conn.setAutoCommit(true);
      return result;
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
          conn.setAutoCommit(true);
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

  @SneakyThrows
  public static Connection connection(String jdbcUrl) {
    return dataSource(jdbcUrl).getConnection();
  }
}
