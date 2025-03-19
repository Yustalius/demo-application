package sdb.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sdb.core.model.user.UserDTO;
import sdb.core.model.validation.UpdateValidationGroup;
import sdb.core.service.UserService;
import utils.logging.Logger;

@RestController
public class UserController {
  @Autowired
  private Logger logger;

  @Autowired
  private UserService userService;

  @GetMapping("/user/{id}")
  public ResponseEntity<UserDTO> getUser(
      @PathVariable int id
  ) {
    logger.info("Get user id = %s".formatted(id));
    UserDTO userJson = userService.get(id);
    return ResponseEntity.ok(userJson);
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteUser(
      @PathVariable int id
  ) {
    logger.info("Delete user id = %s".formatted(id));
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/user/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable int id,
      @Validated(UpdateValidationGroup.class) @RequestBody UserDTO user
  ) {
    UserDTO updatedUser = userService.update(id, user);
    logger.info("Succesfully updated user id = " + id);
    return ResponseEntity.ok(updatedUser);
  }
}
