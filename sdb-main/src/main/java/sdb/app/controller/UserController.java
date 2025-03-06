package sdb.app.controller;

import sdb.app.model.user.UserDTO;
import sdb.app.model.validation.UpdateValidationGroup;
import sdb.app.service.UserService;
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
  public List<UserDTO> getAllUsers() {
    logger.info("Get all users");
    List<UserDTO> users = userService.getUsers();
    return users;
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserDTO> getUser(
      @PathVariable int id
  ) {
    logger.info("Get user userId = %s".formatted(id));
    Optional<UserDTO> userJson = userService.get(id);

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
    Optional<UserDTO> userJson = userService.get(id);
    if (userJson.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    userService.delete(id);
    logger.info("User %s deleted".formatted(userJson.get()));
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/user/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable int id,
      @Validated(UpdateValidationGroup.class) @RequestBody UserDTO updateUser
  ) {
    logger.info("Update user userId = %s, %s".formatted(id, updateUser));
    Optional<UserDTO> user = userService.get(id);

    if (user.isEmpty()) {
      logger.info("Not found user with userId = " + id);
      return ResponseEntity.notFound().build();
    }

    userService.update(id, updateUser);

    UserDTO updatedUser = userService.get(id).get();
    logger.info("Updated user %s to %s".formatted(user.get(), updatedUser));

    return ResponseEntity.ok(updatedUser);
  }
}
