package sdb.app.data.dao;

import sdb.app.data.entity.auth.RegisterEntity;
import sdb.app.data.entity.user.UserEntityOld;

public interface AuthDao {

  UserEntityOld register(RegisterEntity entity);

  UserEntityOld login(RegisterEntity entity);
}
