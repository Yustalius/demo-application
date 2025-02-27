package sdb.service.impl;

import retrofit2.Response;
import sdb.api.UserApi;
import sdb.api.core.RestClient;
import sdb.model.user.UserJson;
import sdb.service.UserClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserApiClient extends RestClient implements UserClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(CFG.userApiUrl());
    this.userApi = create(UserApi.class);
  }

  @Override
  public List<UserJson> getAllUsers() {
    Response<List<UserJson>> response;
    try {
      response = userApi.getAllUsers()
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public UserJson getUser(int id) {
    return null;
  }

  @Override
  public void deleteUser(int id) {

  }

  @Override
  public UserJson updateUser(int id, UserJson user) {
    return null;
  }
}
