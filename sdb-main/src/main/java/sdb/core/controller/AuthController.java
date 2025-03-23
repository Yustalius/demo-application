package sdb.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.core.model.auth.LoginDTO;
import sdb.core.model.auth.RegisterJson;
import sdb.core.model.auth.Token;
import sdb.core.model.error.ErrorResponse;
import sdb.core.model.user.UserDTO;
import sdb.core.model.validation.LoginValidationGroup;
import sdb.core.model.validation.RegistrationValidationGroup;
import sdb.core.service.AuthService;
import utils.logging.Logger;

@RestController
@RequestMapping("/auth")
@Tag(name = "Авторизация", description = "Запросы на логин/регистрацию")
public class AuthController {
  @Autowired
  private Logger logger;

  @Autowired
  private AuthService authService;

  @Operation(summary = "Регистрация")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
          content = @Content(schema = @Schema(implementation = UserDTO.class))),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("/register")
  public UserDTO register(
      @Valid @RequestBody RegisterJson json) {
    UserDTO user = authService.register(json);

    logger.info("Successfully registered user id = ", user.id());
    return user;
  }

  @Operation(summary = "Авторизация")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная авторизация",
          content = @Content(schema = @Schema(implementation = Token.class))
      ),
      @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
      @ApiResponse(responseCode = "401", ref = "InvalidCredsResponse"),
      @ApiResponse(responseCode = "500", ref = "InternalServerErrorResponse")
  })
  @PostMapping("/login")
  public Token login(@Valid @RequestBody LoginDTO login) {
    return new Token(authService.login(login));
  }
}
