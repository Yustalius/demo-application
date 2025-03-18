package sdb.data.dao;


import sdb.data.entity.user.UsersEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {
  UsersEntity create(UsersEntity user);

  Optional<UsersEntity> get(int id);

  List<UsersEntity> getUsers();

  void delete(int id);

  void update(int id, UsersEntity user);
}
