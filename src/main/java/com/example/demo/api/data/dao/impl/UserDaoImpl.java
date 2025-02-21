package com.example.demo.api.data.dao.impl;

import com.example.demo.config.Config;
import com.example.demo.api.data.Databases;
import com.example.demo.api.data.dao.UserDao;
import com.example.demo.api.data.entity.user.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {
  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity user) {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());

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
      }
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't create user");
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
      throw new RuntimeException("Couldn't create user");
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
      throw new RuntimeException("Couldn't create user");
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
