package com.example.demo.api.service;

import com.example.demo.api.model.user.UserJson;

import java.util.Optional;

public interface UserService {

  UserJson create(UserJson user);

  Optional<UserJson> get(int id);

  void delete(int id);

  void update(int userId, UserJson user);
}
