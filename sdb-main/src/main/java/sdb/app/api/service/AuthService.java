package sdb.app.api.service;

import sdb.app.api.model.auth.RegisterJson;

public interface AuthService {

  int register(RegisterJson json);

  void login();
}
