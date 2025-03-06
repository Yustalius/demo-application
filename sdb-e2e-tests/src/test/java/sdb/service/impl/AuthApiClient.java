package sdb.service.impl;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;
import sdb.api.AuthApi;
import sdb.api.core.AuthInterceptor;
import sdb.api.core.RestClient;
import sdb.model.auth.LoginRequest;
import sdb.model.auth.RegisterDTO;
import sdb.model.auth.Token;
import sdb.model.user.UserDTO;
import sdb.service.AuthClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiClient extends RestClient implements AuthClient {

  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.coreUrl());
    this.authApi = create(AuthApi.class);
  }

  @Override
  public UserDTO createNewUser(RegisterDTO json) {
    Response<UserDTO> response;
    try {
      response = authApi.register(json).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public Token login(LoginRequest json) {
    Response<Token> response;
    try {
      response = authApi.login(json).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
