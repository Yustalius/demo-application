package sdb.app.api.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.model.user.UserJson;
import sdb.app.api.service.AuthService;
import sdb.app.logging.Logger;

import javax.sql.DataSource;

import static sdb.app.api.data.Databases.transaction;

@Service
public class AuthServiceImpl implements AuthService {
  private static final Logger logger = new Logger();
  private final AuthDao authDao;
  private final UserDao userDao;
  private final TransactionTemplate transactionTemplate;

  public AuthServiceImpl(
      AuthDao authDao,
      UserDao userDao,
      @Qualifier("dbDatasource") DataSource dataSource
  ) {
    this.authDao = authDao;
    this.userDao = userDao;
    this.transactionTemplate = new TransactionTemplate(new JdbcTransactionManager(dataSource));
  }

  @Override
  public UserJson register(RegisterJson json) {
    logger.info("Starting user creation process for ", json);
    try {
      UserEntity user = transactionTemplate.execute(status -> {
        UserEntity registeredUser = authDao.register(RegisterEntity.fromJson(json));

        return userDao.create(registeredUser);
      });
      return UserJson.fromEntity(user);
    } catch (TransactionException e) {
      logger.error("Couldn't create user ", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserJson login(RegisterJson json) {
    return UserJson.fromEntity(
        authDao.login(RegisterEntity.fromJson(json)));
  }
}
