package com.example.demo.api.data.dao;

import com.example.demo.api.data.entity.user.UserEntity;
import com.example.demo.api.model.user.UserJson;

import java.util.List;
import java.util.Optional;

public interface UserDao {
  UserEntity create(UserEntity user);

  Optional<UserEntity> get(int id);

  List<UserEntity> getUsers();

  void delete(int id);

  void update(int id, UserEntity user);
}
