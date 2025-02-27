package sdb.service.impl;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import retrofit2.Response;
import sdb.api.UserApi;
import sdb.api.core.RestClient;
import sdb.model.user.UserDTO;
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
  public List<UserDTO> getAllUsers() {
    Response<List<UserDTO>> response;
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
  public UserDTO getUser(int id) {
    Response<UserDTO> response;
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
  public UserDTO updateUser(int id, @NonNull UserDTO user) {
    Response<UserDTO> response;
    try {
      response = userApi.updateUser(id, user).execute();
    } catch (IOException e) {
      throw new AssertionError("Couldn't update user", e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
