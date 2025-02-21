package sdb.app.api.data.dao;

import sdb.app.api.data.entity.auth.RegisterEntity;

public interface AuthDao {

  int register(RegisterEntity entity);

  void login();
}
