package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.data.mapper.UserEntityRowMapper;
import sdb.app.ex.UserNotFoundException;
import sdb.app.logging.Logger;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class AuthDaoSpringImpl implements AuthDao {
  @Autowired
  private Logger logger;

  private final JdbcTemplate jdbcTemplate;

  public AuthDaoSpringImpl(@Qualifier("dbDatasource") DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public UserEntity register(RegisterEntity entity) {
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

    UserEntity user = new UserEntity();
    user.setId((Integer) requireNonNull(keyHolder.getKeys().get("id")));
    user.setFirstName(entity.getFirstName());
    user.setLastName(entity.getLastName());
    user.setAge(entity.getAge());
    return user;
  }

  @Override
  public UserEntity login(RegisterEntity entity) {
    try {
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
    } catch (EmptyResultDataAccessException e) {
      logger.warn("Not found user ", entity);
      throw new UserNotFoundException("Not found user %s".formatted(entity));
    }
  }
}
