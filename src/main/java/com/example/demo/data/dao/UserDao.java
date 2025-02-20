package com.example.demo.data.dao;

import com.example.demo.data.entity.user.UserEntity;

import java.util.Optional;

public interface UserDao {
  UserEntity create(UserEntity user);

  Optional<UserEntity> get(int id);

  void delete(int id);

  void update(int id, UserEntity user);
}
