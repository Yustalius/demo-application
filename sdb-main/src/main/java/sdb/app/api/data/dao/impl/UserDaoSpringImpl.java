package sdb.app.api.data.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.data.mapper.UserEntityRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

//@Component
public class UserDaoSpringImpl implements UserDao {

  private final DataSource dataSource;

  public UserDaoSpringImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public UserEntity create(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
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
    }, kh);

    final int userId = (int) kh.getKeys().get("id");
    user.setId(userId);

    return user;
  }

  @Override
  public Optional<UserEntity> get(int id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"users\" WHERE userId = ?",
            UserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<UserEntity> getUsers() {
    return List.of();
  }

  @Override
  public void delete(int id) {

  }

  @Override
  public void update(int id, UserEntity user) {

  }
}
