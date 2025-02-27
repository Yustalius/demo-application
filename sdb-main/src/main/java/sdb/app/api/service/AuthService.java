package sdb.app.api.service;

import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.model.user.UserJson;

public interface AuthService {

  UserJson register(RegisterJson json);

  UserJson login(RegisterJson json);
}
