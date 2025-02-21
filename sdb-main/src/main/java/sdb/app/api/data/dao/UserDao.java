package sdb.app.api.data.dao;

import sdb.app.api.data.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {
  void create(UserEntity user);

  Optional<UserEntity> get(int id);

  List<UserEntity> getUsers();

  void delete(int id);

  void update(int id, UserEntity user);
}
