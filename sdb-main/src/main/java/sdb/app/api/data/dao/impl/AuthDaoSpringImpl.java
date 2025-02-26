package sdb.app.api.data.dao.impl;

import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.entity.auth.RegisterEntity;

//@Component
public class AuthDaoSpringImpl implements AuthDao {
  @Override
  public int register(RegisterEntity entity) {
    return 0;
  }

  @Override
  public void login() {

  }
}
