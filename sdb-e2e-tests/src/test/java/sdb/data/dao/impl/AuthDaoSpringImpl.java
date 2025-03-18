package sdb.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sdb.data.dao.AuthDao;
import sdb.data.entity.auth.RegisterEntity;
import sdb.data.entity.user.UsersEntity;
import sdb.data.mapper.UserEntityRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static java.util.Objects.requireNonNull;

public class AuthDaoSpringImpl implements AuthDao {
  private final JdbcTemplate jdbcTemplate;

  public AuthDaoSpringImpl(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public UsersEntity register(RegisterEntity entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO user_creds (username, pass) VALUES (?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, entity.getUsername());
      ps.setString(2, entity.getPassword());
      return ps;
    }, keyHolder);

    UsersEntity user = new UsersEntity();
    user.setId((Integer) requireNonNull(keyHolder.getKeys().get("id")));
    user.setFirstName(entity.getFirstName());
    user.setLastName(entity.getLastName());
    user.setAge(entity.getAge());
    return user;
  }

  @Override
  public UsersEntity login(RegisterEntity entity) {
    return jdbcTemplate.queryForObject(
        """
            SELECT users.id, users.first_name, users.last_name, users.age 
            FROM users 
            JOIN user_creds uc ON users.id = uc.id 
            WHERE uc.username = ? AND uc.pass = ?
            """,
        UserEntityRowMapper.instance,
        entity.getUsername(),
        entity.getPassword()
    );
  }
}
