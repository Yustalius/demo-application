package sdb.app.service;

import sdb.app.model.user.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

  void create(UserDTO user);

  Optional<UserDTO> get(int id);

  List<UserDTO> getUsers();

  void delete(int id);

  void update(int userId, UserDTO user);
}
