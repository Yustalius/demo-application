package sdb.app.api.data.dao.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import sdb.app.config.Config;
import sdb.app.api.data.Databases;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.user.UserEntity;
import org.springframework.stereotype.Component;
import sdb.app.logging.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Component
public class UserDaoImpl implements UserDao {
  private static final Config CFG = Config.getInstance();
  private static final Logger logger = new Logger();

  private final Connection connection;

  public UserDaoImpl(@Qualifier("dbConnection") Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity create(UserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO users (id, first_name, last_name, age) VALUES (?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setInt(1, user.getId());
      ps.setString(2, user.getFirstName());
      ps.setString(3, user.getLastName());
      ps.setInt(4, user.getAge());

      ps.executeUpdate();

      final int userId;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          userId = rs.getObject("id", Integer.class);
        } else {
          throw new SQLException("Can't find id in result set");
        }
      }

      user.setId(userId);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> get(int id) {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * FROM users WHERE id = ?"
      )) {
        ps.setInt(1, id);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            UserEntity user = new UserEntity();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setAge(rs.getInt("age"));

            return Optional.of(user);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't get user from database");
    }
  }

  @Override
  public List<UserEntity> getUsers() {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * FROM users"
      )) {
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          List<UserEntity> users = new ArrayList<>();
          while (rs.next()) {
            UserEntity user = new UserEntity();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setAge(rs.getInt("age"));

            users.add(user);
          }

          return users;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't get user from database");
    }
  }

  @Override
  public void delete(int id) {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "DELETE FROM users WHERE id = ?"
      )) {
        ps.setInt(1, id);
        ps.execute();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't delete user");
    }
  }

  @Override
  public void update(int userId, UserEntity user) {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          updateUserSql(userId, user)
      )) {
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't update user", e);
    }
  }

  public static String updateUserSql(int userId, UserEntity userEntity) {
    StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");

    boolean hasUpdates = false;

    if (userEntity.getFirstName() != null) {
      sqlBuilder.append("first_name = '%s'".formatted(userEntity.getFirstName()));
      hasUpdates = true;
    }

    if (userEntity.getLastName() != null) {
      if (hasUpdates) sqlBuilder.append(", ");
      sqlBuilder.append("last_name = '%s'".formatted(userEntity.getLastName()));
      hasUpdates = true;
    }

    if (userEntity.getAge() != null) {
      if (hasUpdates) sqlBuilder.append(", ");
      sqlBuilder.append("age = %s".formatted(userEntity.getAge()));
      hasUpdates = true;
    }

    if (!hasUpdates) {
      throw new IllegalArgumentException("No fields to update");
    }

    sqlBuilder.append(" WHERE id = %s".formatted(userId));
    return sqlBuilder.toString();
  }
}
