package sdb.app.api.controller;

import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.model.user.UserJson;
import sdb.app.api.service.AuthService;
import sdb.app.api.service.UserService;
import sdb.app.api.service.impl.AuthServiceImpl;
import sdb.app.api.service.impl.UserServiceImpl;
import sdb.app.logging.Logger;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final Logger logger = new Logger();

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserJson> register(@RequestBody RegisterJson json) {
    logger.info("Register user " + json);
    try {
      int id = authService.register(json);
      logger.info("Successfully registered user id = ", id);
      Optional<UserJson> userJson = userService.get(id);
      return ResponseEntity.ok(userJson.get());
    } catch (Exception e) {
      logger.error("Error registering user " + e.toString());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping("/login")
  public void login() {

  }
}
