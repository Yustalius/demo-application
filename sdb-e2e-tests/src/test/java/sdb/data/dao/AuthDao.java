package sdb.data.dao;

import sdb.data.entity.auth.RegisterEntity;
import sdb.data.entity.user.UserEntity;

public interface AuthDao {

  UserEntity register(RegisterEntity entity);

  UserEntity login(RegisterEntity entity);
}
