package sdb.app.api.controller;

import sdb.app.api.model.user.UserJson;
import sdb.app.api.model.validation.CreateValidationGroup;
import sdb.app.api.model.validation.UpdateValidationGroup;
import sdb.app.api.service.UserService;
import sdb.app.api.service.impl.UserServiceImpl;
import sdb.app.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
  @Autowired
  private Logger logger;

  @Autowired
  private UserService userService;

  @GetMapping("/user")
  public List<UserJson> getAllUsers() {
    logger.info("Get all users");
    List<UserJson> users = userService.getUsers();
    return users;
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserJson> getUser(
      @PathVariable int id
  ) {
    logger.info("Get user userId = %s".formatted(id));
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
    logger.info("Delete user userId = %s".formatted(id));
    Optional<UserJson> userJson = userService.get(id);
    if (userJson.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    userService.delete(id);
    logger.info("User %s deleted".formatted(userJson.get()));
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/user/{id}")
  public ResponseEntity<UserJson> updateUser(
      @PathVariable int id,
      @Validated(UpdateValidationGroup.class) @RequestBody UserJson updateUser
  ) {
    logger.info("Update user userId = %s, %s".formatted(id, updateUser));
    Optional<UserJson> user = userService.get(id);

    if (user.isEmpty()) {
      logger.info("Not found user with userId = " + id);
      return ResponseEntity.notFound().build();
    }

    userService.update(id, updateUser);

    UserJson updatedUser = userService.get(id).get();
    logger.info("Updated user %s to %s".formatted(user.get(), updatedUser));

    return ResponseEntity.ok(updatedUser);
  }
}
