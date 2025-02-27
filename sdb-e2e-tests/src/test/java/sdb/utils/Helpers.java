package sdb.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import sdb.config.Config;

import java.util.Objects;

import static sdb.data.Databases.dataSource;

public class Helpers {
  private static final Config CFG = Config.getInstance();

  public static int userIdByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(CFG.postgresUrl()));
    return Objects.requireNonNull(
        jdbcTemplate.queryForObject(
            "SELECT id FROM \"user_creds\" WHERE username = ?",
            Integer.class,
            username
        ));
  }

  public static String usernameByUserId(int id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(CFG.postgresUrl()));
    return Objects.requireNonNull(
        jdbcTemplate.queryForObject(
            "SELECT username FROM \"user_creds\" WHERE id = ?",
            String.class,
            id
        ));
  }
}
