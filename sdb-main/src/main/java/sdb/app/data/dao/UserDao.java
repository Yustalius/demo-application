package sdb.app.data.dao;

import sdb.app.data.entity.user.UserEntityOld;

import java.util.List;
import java.util.Optional;

public interface UserDao {
  UserEntityOld create(UserEntityOld user);

  Optional<UserEntityOld> get(int id);

  List<UserEntityOld> getUsers();

  void delete(int id);

  void update(int id, UserEntityOld user);
}
