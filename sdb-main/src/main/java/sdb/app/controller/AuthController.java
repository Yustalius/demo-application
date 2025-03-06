package sdb.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.app.model.auth.RegisterJson;
import sdb.app.model.auth.Token;
import sdb.app.model.user.UserDTO;
import sdb.app.model.validation.LoginValidationGroup;
import sdb.app.model.validation.RegistrationValidationGroup;
import sdb.app.service.AuthService;
import sdb.app.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private Logger logger;

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(
      @Validated(RegistrationValidationGroup.class) @RequestBody RegisterJson json) {
    UserDTO user = authService.register(json);

    logger.info("Successfully registered user id = ", user.id());
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public Token login(
      @Validated(LoginValidationGroup.class) @RequestBody RegisterJson json
  ) {
    return new Token(authService.login(json));
  }
}
