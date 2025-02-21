package sdb.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.service.AuthService;
import sdb.app.logging.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final Logger logger = new Logger();

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public void register(@RequestBody RegisterJson json) {
    authService.register(json);
  }

  @PostMapping("/login")
  public void login() {

  }
}
