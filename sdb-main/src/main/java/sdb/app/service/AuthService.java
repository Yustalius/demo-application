package sdb.app.service;

import sdb.app.model.auth.RegisterJson;
import sdb.app.model.user.UserDTO;

public interface AuthService {

  UserDTO register(RegisterJson json);

  String login(RegisterJson json);
}
