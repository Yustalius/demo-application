package sdb.app.service.impl;

import sdb.app.data.dao.UserDao;
import sdb.app.data.entity.user.UserEntityOld;
import sdb.app.model.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.app.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private UserDao userDao;

  @Override
  public void create(UserDTO json) {
    userDao.create(UserEntityOld.fromJson(json));
  }

  @Override
  public Optional<UserDTO> get(int id) {
    return userDao.get(id).map(UserDTO::fromEntity);
  }

  @Override
  public List<UserDTO> getUsers() {
    return userDao.getUsers().stream()
        .sorted(Comparator.comparing(UserEntityOld::getId))
        .map(UserDTO::fromEntity)
        .toList();
  }

  @Override
  public void delete(int id) {
    userDao.delete(id);
  }

  @Override
  public void update(int userId, UserDTO user) {
    UserEntityOld userEntity = UserEntityOld.fromJson(user);
    userDao.update(userId, userEntity);
  }
}
