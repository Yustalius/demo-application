package sdb.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sdb.core.model.user.UserDTO;
import sdb.core.model.validation.UpdateValidationGroup;
import sdb.core.service.UserService;
import utils.logging.Logger;

@RestController
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {
  @Autowired
  private Logger logger;

  @Autowired
  private UserService userService;

  @Operation(summary = "Получение пользователя по ID", description = "Получает пользователя по его ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно получен",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "UserNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @GetMapping("/user/{id}")
  public ResponseEntity<UserDTO> getById(
      @PathVariable int id
  ) {
    logger.info("Get user id = %s".formatted(id));
    UserDTO userJson = userService.get(id);
    return ResponseEntity.ok(userJson);
  }

  @Operation(summary = "Удаление пользователя по ID", description = "Удаляет пользователя по его ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "403", ref = "PermissionDeniedResponse"),
      @ApiResponse(responseCode = "404", ref = "UserNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable int id) {
    logger.info("Delete user id = %s".formatted(id));
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Обновление пользователя по ID", description = "Обновляет пользователя по его ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "NotAuthorizedResponse"),
      @ApiResponse(responseCode = "404", ref = "UserNotFoundResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PatchMapping("/user/{id}")
  public ResponseEntity<UserDTO> update(
      @PathVariable int id,
      @Validated(UpdateValidationGroup.class) @RequestBody UserDTO user) {
    UserDTO updatedUser = userService.update(id, user);
    logger.info("Succesfully updated user id = " + id);
    return ResponseEntity.ok(updatedUser);
  }
}
