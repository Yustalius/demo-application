package sdb.app.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthDao authDao;

  @Override
  public void register(RegisterJson json) {
    authDao.register(RegisterEntity.fromJson(json));
  }

  @Override
  public void login() {
    System.out.println("login");
  }
}
