package sdb.service;

import sdb.data.entity.auth.RegisterEntity;
import sdb.model.auth.RegisterJson;
import sdb.model.user.UserDTO;

public interface AuthClient {

/*  static AuthClient getInstance() {
    return new AuthApiClient();
  }*/

  UserDTO createNewUser(RegisterEntity json);

  UserDTO login(RegisterJson json);

}
