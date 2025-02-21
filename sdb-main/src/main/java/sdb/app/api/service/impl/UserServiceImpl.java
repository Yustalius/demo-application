package sdb.app.api.service.impl;

import sdb.app.api.data.dao.UserDao;
import sdb.app.api.data.entity.user.UserEntity;
import sdb.app.api.model.user.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.app.api.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Override
  public void create(UserJson json) {
    userDao.create(UserEntity.fromJson(json));
  }

  @Override
  public Optional<UserJson> get(int id) {
    return userDao.get(id).map(UserJson::fromEntity);
  }

  @Override
  public List<UserJson> getUsers() {
    return userDao.getUsers().stream()
        .sorted(Comparator.comparing(UserEntity::getId))
        .map(UserJson::fromEntity)
        .toList();
  }

  @Override
  public void delete(int id) {
    userDao.delete(id);
  }

  @Override
  public void update(int userId, UserJson user) {
    UserEntity userEntity = UserEntity.fromJson(user);
    userDao.update(userId, userEntity);
  }
}
