package sdb.app.api.service.impl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.dao.impl.AuthDaoImpl;
import sdb.app.api.data.dao.impl.UserDaoImpl;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.service.AuthService;
import sdb.app.config.AppConfig;
import sdb.app.config.Config;
import sdb.app.logging.Logger;

import java.sql.Connection;

import static sdb.app.api.data.Databases.transaction;

@Service
public class AuthServiceImpl implements AuthService {
  private static final Logger logger = new Logger();

  @Autowired
  private AuthDao authDao;

  @Autowired
  private UserDao userDao;

  @Override
  public int register(RegisterJson json) {
    logger.info("Starting user creation process for ", json);
    try {
      return transaction(() -> {
        int id = authDao.register(RegisterEntity.fromJson(json));

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFirstName(json.firstName());
        userEntity.setLastName(json.lastName());
        userEntity.setAge(json.age());

        userDao.create(userEntity);

        return id;
      }, new AnnotationConfigApplicationContext(AppConfig.class).getBean("dbConnection", Connection.class));
    } catch (Exception e) {
      logger.error("Error during user creation ", e);
      throw new RuntimeException("Failed to create user", e);
    }
  }

  @Override
  public void login() {
    System.out.println("login");
  }
}
