package sdb.data.dao;

import sdb.data.entity.auth.RegisterEntity;
import sdb.data.entity.user.UsersEntity;

public interface AuthDao {

  UsersEntity register(RegisterEntity entity);

  UsersEntity login(RegisterEntity entity);
}
