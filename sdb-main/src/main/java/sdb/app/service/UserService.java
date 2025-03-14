package sdb.app.service;

import sdb.app.model.user.UserDTO;

public interface UserService {

  void create(UserDTO user);

  UserDTO get(int id);

  void delete(int id);

  UserDTO update(int userId, UserDTO user);
}
