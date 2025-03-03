package sdb.app.service;

import sdb.app.model.auth.RegisterJson;
import sdb.app.model.user.UserJson;

public interface AuthService {

  UserJson register(RegisterJson json);

  UserJson login(RegisterJson json);
}
