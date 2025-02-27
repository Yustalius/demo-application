package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.ex.DuplicateUsernameException;
import sdb.app.logging.Logger;

import java.sql.*;

//@Component
public class AuthDaoImpl implements AuthDao {
  private static final Logger logger = new Logger();

  private final Connection connection;

  public AuthDaoImpl(@Qualifier("dbConnection") Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity register(RegisterEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO user_creds (username, pass) VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, entity.getUsername());
      ps.setString(2, entity.getPassword());
      ps.executeUpdate();

      final int userId;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          userId = rs.getObject("id", Integer.class);
        } else {
          logger.error("Not found userId in result set");
          throw new SQLException("Can't find userId in result set");
        }
      }

      UserEntity user = new UserEntity();
      user.setId(userId);
      user.setFirstName(entity.getFirstName());
      user.setLastName(entity.getLastName());
      user.setAge(entity.getAge());
      return user;
    } catch (SQLException e) {
      if ("23505".equals(e.getSQLState())) {
        logger.error("Registration failed: duplicate username " + entity.getUsername());
        throw new DuplicateUsernameException("Duplicate username " + entity.getUsername());
      }
      logger.error("Registration failed for user %s ".formatted(entity), e);
      throw new RuntimeException("Registration failed", e);
    }
  }

  @Override
  public UserEntity login(RegisterEntity entity) {

    return null;
  }
}
