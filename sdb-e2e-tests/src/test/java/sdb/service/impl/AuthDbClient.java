package sdb.service.impl;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.config.Config;
import sdb.data.dao.AuthDao;
import sdb.data.dao.UserDao;
import sdb.data.dao.impl.AuthDaoSpringImpl;
import sdb.data.dao.impl.UserDaoSpringImpl;
import sdb.data.entity.auth.RegisterEntity;
import sdb.data.entity.user.UserEntity;
import sdb.model.auth.RegisterDTO;
import sdb.model.user.UserDTO;

import javax.sql.DataSource;

public class AuthDbClient {
  private static final Config CFG = Config.getInstance();

  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;
  private final AuthDao authDao;
  private final UserDao userDao;

  public AuthDbClient(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.transactionTemplate = new TransactionTemplate(new JdbcTransactionManager(dataSource));
    this.authDao = new AuthDaoSpringImpl(dataSource);
    this.userDao = new UserDaoSpringImpl(dataSource);
  }

  @NonNull
  public UserDTO createNewUser(RegisterEntity entity) {
    UserEntity user = transactionTemplate.execute(status -> {
      UserEntity registeredUser = authDao.register(entity);

      return userDao.create(registeredUser);
    });

    return UserDTO.fromEntity(user);
  }

  public UserDTO login(RegisterDTO json) {
    return null;
  }
}
