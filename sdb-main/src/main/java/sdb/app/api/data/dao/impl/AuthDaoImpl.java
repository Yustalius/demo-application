package sdb.app.api.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.api.data.Databases;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.config.Config;
import sdb.app.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
  public void register(RegisterEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO user_creds (username, pass) VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, entity.getUsername());
      ps.setString(2, entity.getPassword());

      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Registration failed", e);
    }
  }

  @Override
  public void login() {

  }
}
