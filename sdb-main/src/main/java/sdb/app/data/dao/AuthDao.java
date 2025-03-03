package sdb.app.data.dao;

import sdb.app.data.entity.auth.RegisterEntity;
import sdb.app.data.entity.user.UserEntity;

public interface AuthDao {

  UserEntity register(RegisterEntity entity);

  UserEntity login(RegisterEntity entity);
}
