package sdb.data;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Databases {
  private Databases() {
  }

  private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();

  public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {
  }

  public record XaConsumer<T>(Consumer<Connection> function, String jdbcUrl) {
  }

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

  public static DataSource dataSource(String jdbcUrl) {
    return datasources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          String uniqueId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqueId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "1234");
          dsBean.setXaProperties(props);
          dsBean.setMaxPoolSize(10);
          try {
            InitialContext context = new InitialContext();
            context.bind("java:comp/env/jdbc/" + uniqueId, dsBean);
          } catch (NamingException e) {
            throw new RuntimeException(e);
          }

          return dsBean;
        }
    );
  }

  private static DataSource xaDataSource(String jdbcUrl) {
    return datasources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          String uniqueId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqueId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "1234");
          dsBean.setXaProperties(props);
          dsBean.setMaxPoolSize(15);
          try {
            InitialContext context = new InitialContext();
            context.bind("java:comp/env/jdbc/" + uniqueId, dsBean);
          } catch (NamingException e) {
            throw new RuntimeException(e);
          }

          return dsBean;
        }
    );
  }

  @SneakyThrows
  public static Connection connection(String jdbcUrl) {
    return xaDataSource(jdbcUrl).getConnection();
  }
}