package sdb.app.service;

import sdb.app.model.user.UserJson;

import java.util.List;
import java.util.Optional;

public interface UserService {

  void create(UserJson user);

  Optional<UserJson> get(int id);

  List<UserJson> getUsers();

  void delete(int id);

  void update(int userId, UserJson user);
}
