package sdb.core.service;

import sdb.core.model.auth.LoginDTO;
import sdb.core.model.auth.RegisterJson;
import sdb.core.model.user.UserDTO;

public interface AuthService {

  UserDTO register(RegisterJson json);

  String login(LoginDTO json);
}
