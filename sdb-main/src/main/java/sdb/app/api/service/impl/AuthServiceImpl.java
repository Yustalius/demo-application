package sdb.app.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.app.api.data.dao.AuthDao;
import sdb.app.api.data.dao.impl.AuthDaoImpl;
import sdb.app.api.data.entity.auth.RegisterEntity;
import sdb.app.api.model.auth.RegisterJson;
import sdb.app.api.service.AuthService;
import sdb.app.config.Config;

import static sdb.app.api.data.Databases.transaction;

@Service
public class AuthServiceImpl implements AuthService {
  private static Config CFG = Config.getInstance();

  @Override
  public void register(RegisterJson json) {
    transaction(connection -> {
      new AuthDaoImpl(connection).register(RegisterEntity.fromJson(json));
    }, CFG.postgresUrl());
  }

  @Override
  public void login() {
    System.out.println("login");
  }
}
