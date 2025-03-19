package sdb.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.core.model.auth.RegisterJson;
import sdb.core.model.auth.Token;
import sdb.core.model.user.UserDTO;
import sdb.core.model.validation.LoginValidationGroup;
import sdb.core.model.validation.RegistrationValidationGroup;
import sdb.core.service.AuthService;
import utils.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private Logger logger;

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public UserDTO register(
      @Validated(RegistrationValidationGroup.class) @RequestBody RegisterJson json) {
    UserDTO user = authService.register(json);

    logger.info("Successfully registered user id = ", user.id());
    return user;
  }

  @PostMapping("/login")
  public Token login(
      @Validated(LoginValidationGroup.class) @RequestBody RegisterJson json
  ) {
    return new Token(authService.login(json));
  }
}
