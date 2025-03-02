package sdb.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.model.user.UserJson;
import sdb.app.api.model.validation.LoginValidationGroup;
import sdb.app.api.model.validation.RegistrationValidationGroup;
import sdb.app.api.service.AuthService;
import sdb.app.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private Logger logger;

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserJson> register(
      @Validated(RegistrationValidationGroup.class) @RequestBody RegisterJson json) {
    logger.info("Starting register process for user " + json);
    UserJson user = authService.register(json);

    logger.info("Successfully registered user id = ", user.id());
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<UserJson> login(
      @Validated(LoginValidationGroup.class) @RequestBody RegisterJson json
  ) {
    return ResponseEntity.ok(authService.login(json));
  }
}
