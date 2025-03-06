package sdb.service;

import sdb.model.auth.LoginRequest;
import sdb.model.auth.RegisterDTO;
import sdb.model.auth.Token;
import sdb.model.user.UserDTO;
import sdb.service.impl.AuthApiClient;

public interface AuthClient {

  static AuthClient getInstance() {
    return new AuthApiClient();
  }

  UserDTO createNewUser(RegisterDTO json);

  Token login(LoginRequest json);
}
