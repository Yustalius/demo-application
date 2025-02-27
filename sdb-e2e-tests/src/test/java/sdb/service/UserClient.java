package sdb.service;

import sdb.model.user.UserJson;
import sdb.service.impl.UserApiClient;

import java.util.List;

public interface UserClient {

  static UserClient getInstance() {
    return new UserApiClient();
  }

  List<UserJson> getAllUsers();

  UserJson getUser(int id);

  void deleteUser(int id);

  UserJson updateUser(
      int id,
      UserJson user
  );
}
