package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.data.mapper.UserEntityRowMapper;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoSpringImpl implements UserDao {
  private final JdbcTemplate jdbcTemplate;

  public UserDaoSpringImpl(@Qualifier("dbDatasource") DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public UserEntity create(UserEntity user) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO users (id, first_name, last_name, age) VALUES (?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setInt(1, user.getId());
      ps.setString(2, user.getFirstName());
      ps.setString(3, user.getLastName());
      ps.setInt(4, user.getAge());
      return ps;
    }, keyHolder);

    user.setId(keyHolder.getKey().intValue());
    return user;
  }

  @Override
  public Optional<UserEntity> get(int id) {
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"users\" WHERE id = ?",
            UserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<UserEntity> getUsers() {
    return jdbcTemplate.query("SELECT * FROM users", UserEntityRowMapper.instance);
  }

  @Override
  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM user_creds WHERE id = ?", id);
  }

  @Override
  public void update(int userId, UserEntity user) {
    StringBuilder sql = new StringBuilder("UPDATE users SET ");
    List<Object> params = new ArrayList<>();

    if (user.getFirstName() != null) {
      sql.append("first_name = ?, ");
      params.add(user.getFirstName());
    }
    if (user.getLastName() != null) {
      sql.append("last_name = ?, ");
      params.add(user.getLastName());
    }
    if (user.getAge() != null) {
      sql.append("age = ?, ");
      params.add(user.getAge());
    }

    if (params.isEmpty()) {
      throw new IllegalArgumentException("No fields to update");
    }
    sql.setLength(sql.length() - 2);
    sql.append(" WHERE id = ?");
    params.add(userId);

    jdbcTemplate.update(sql.toString(), params.toArray());
  }
}