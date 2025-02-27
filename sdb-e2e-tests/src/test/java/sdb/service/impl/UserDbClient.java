package sdb.service.impl;

import sdb.model.user.UserDTO;
import sdb.service.UserClient;

import java.util.List;

public class UserDbClient implements UserClient {
  @Override
  public List<UserDTO> getAllUsers() {
    return List.of();
  }

  @Override
  public UserDTO getUser(int id) {
    return null;
  }

  @Override
  public void deleteUser(int id) {

  }

  @Override
  public UserDTO updateUser(int id, UserDTO user) {
    return null;
  }
}
