package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.config.Config;
import sdb.app.logging.Logger;

import java.sql.*;

//@Component
public class AuthDaoImpl implements AuthDao {
  private static final Config CFG = Config.getInstance();
  private static final Logger logger = new Logger();

  private final Connection connection;

  public AuthDaoImpl(Connection connection) {
    this.connection = connection;
  }

  @Autowired
  private UserDao userDao;

  @Override
  public int register(RegisterEntity entity) {
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
          throw new SQLException("Can't find id in result set");
        }
      }

      return userId;
    } catch (SQLException e) {
      throw new RuntimeException("Registration failed", e);
    }
  }

  @Override
  public void login() {

  }
}
