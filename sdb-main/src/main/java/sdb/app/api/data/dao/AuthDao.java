package sdb.app.api.data.dao;

import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.data.entity.user.UserEntity;

public interface AuthDao {

  UserEntity register(RegisterEntity entity);

  UserEntity login(RegisterEntity entity);
}
