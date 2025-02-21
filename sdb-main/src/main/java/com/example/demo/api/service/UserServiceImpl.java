package com.example.demo.api.service;

import com.example.demo.api.data.dao.UserDao;
import com.example.demo.api.data.entity.user.UserEntity;
import com.example.demo.api.model.user.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Override
  public UserJson create(UserJson user) {
    UserEntity userEntity = UserEntity.fromJson(user);
    return UserJson.fromEntity(
        userDao.create(userEntity));
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
