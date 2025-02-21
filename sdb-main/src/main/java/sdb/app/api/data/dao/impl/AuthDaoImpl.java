package sdb.app.api.data.dao.impl;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.api.data.Databases;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.config.Config;
import sdb.app.logging.Logger;

import java.sql.*;

@Component
public class AuthDaoImpl implements AuthDao {
  private static final Config CFG = Config.getInstance();
  private static final Logger logger = new Logger();

  @Autowired
  private UserDao userDao;

  @Override
  public void register(RegisterEntity entity) {
    try (Connection connection = Databases.connection(CFG.postgresUrl())) {
//      connection.setAutoCommit(false);

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

        try {
          UserEntity userEntity = new UserEntity();
          userEntity.setId(userId);
          userEntity.setFirstName(entity.getFirstName());
          userEntity.setLastName(userEntity.getLastName());
          userEntity.setAge(entity.getAge());
          userDao.create(userEntity);
        } catch (Exception e) {
          throw new SQLException("Failed creating user");
        }

//        connection.commit();
      } catch (SQLException e) {
//        connection.rollback();
        throw new SQLException("Registration failed", e);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database error", e);
    }
  }

  @Override
  public void login() {

  }
}
