package sdb.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final Logger logger = new Logger();

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public UserJson register(@RequestBody RegisterJson json) {
    int id = authService.register(json);
    return userService.get(id).get();
  }

  @PostMapping("/login")
  public void login() {

  }
}
