package sdb.core.data.dao;

import sdb.core.data.entity.auth.RegisterEntity;
import sdb.core.data.entity.user.UserEntityOld;

public interface AuthDao {

  UserEntityOld register(RegisterEntity entity);

  UserEntityOld login(RegisterEntity entity);
}
