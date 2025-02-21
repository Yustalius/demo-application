package com.example.demo.api.controller;

import com.example.demo.api.model.user.UserJson;
import com.example.demo.api.model.validation.CreateValidationGroup;
import com.example.demo.api.model.validation.UpdateValidationGroup;
import com.example.demo.api.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UserServiceImpl userService;

  @PostMapping("/user")
  public ResponseEntity<UserJson> createUser(
      @Validated(CreateValidationGroup.class) @RequestBody UserJson user
  ) {
    logger.info("Creating user {}", user);
    UserJson createdUser = userService.create(user);
    return ResponseEntity.ok(createdUser);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserJson> getUser(
      @PathVariable int id
  ) {
    logger.info("Get user id = {}", id);
    Optional<UserJson> userJson = userService.get(id);

    if (userJson.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userJson.get());
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteUser(
      @PathVariable int id
  ) {
    logger.info("Delete user id = {}", id);
    Optional<UserJson> userJson = userService.get(id);
    if (userJson.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/user/{id}")
  public ResponseEntity<UserJson> updateUser(
      @PathVariable int id,
      @Validated(UpdateValidationGroup.class) @RequestBody UserJson updateUser
  ) {
    logger.info("Update user id = {}, {}", id, updateUser);
    Optional<UserJson> user = userService.get(id);

    if (user.isEmpty()) return ResponseEntity.notFound().build();

    userService.update(id, updateUser);
    return ResponseEntity.ok(userService.get(id).get());
  }
}
