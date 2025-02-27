package sdb.service;

import sdb.model.auth.RegisterDTO;
import sdb.model.user.UserDTO;
import sdb.service.impl.AuthApiClient;

public interface AuthClient {

  static AuthClient getInstance() {
    return new AuthApiClient();
  }

  UserDTO createNewUser(RegisterDTO json);

  UserDTO login(RegisterDTO json);

}
