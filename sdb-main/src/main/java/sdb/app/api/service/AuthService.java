package sdb.app.api.service;

import sdb.app.api.model.auth.RegisterJson;

public interface AuthService {

  void register(RegisterJson json);

  void login();
}
