package sdb.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.app.data.dao.AuthDao;
import sdb.app.data.dao.UserDao;
import sdb.app.data.entity.auth.RegisterEntity;
import sdb.app.data.entity.user.UserEntity;
import sdb.app.model.auth.RegisterJson;
import sdb.app.model.user.UserJson;
import sdb.app.service.AuthService;
import sdb.app.logging.Logger;

import javax.sql.DataSource;

import static sdb.app.data.Databases.transaction;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private Logger logger;

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
