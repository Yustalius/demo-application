package sdb.service.impl;

import jakarta.annotation.Nullable;
import lombok.NonNull;
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
    super(CFG.coreUrl());
    this.userApi = create(UserApi.class);
  }

  @Override
  public List<UserJson> getAllUsers() {
    Response<List<UserJson>> response;
    try {
      response = userApi.getAllUsers().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  @Nullable
  public UserJson getUser(int id) {
    Response<UserJson> response;
    try {
      response = userApi.getUser(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }

    if (response.code() == 404) {
      return null;
    }

    assertThat(response.body().id()).isEqualTo(id);
    return response.body();
  }

  @Override
  public void deleteUser(int id) {
    Response<Void> response;
    try {
      response = userApi.deleteUser(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(204);
  }

  @Override
  public UserJson updateUser(int id, @NonNull UserJson user) {
    Response<UserJson> response;
    try {
      response = userApi.updateUser(id, user).execute();
    } catch (IOException e) {
      throw new AssertionError("Couldn't update user", e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
