package sdb.service;

import sdb.model.user.UserDTO;
import sdb.service.impl.UserApiClient;

import java.util.List;

public interface UserClient {

  static UserClient getInstance() {
    return new UserApiClient();
  }

  List<UserDTO> getAllUsers();

  UserDTO getUser(int id);

  void deleteUser(int id);

  UserDTO updateUser(
      int id,
      UserDTO user
  );
}
