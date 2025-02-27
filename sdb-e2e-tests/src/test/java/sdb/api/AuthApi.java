package sdb.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import sdb.model.auth.RegisterJson;
import sdb.model.user.UserDTO;

public interface AuthApi {
  @POST("auth/register")
  Call<UserDTO> register(@Body RegisterJson json);

  @POST("auth/login")
  Call<UserDTO> login(@Body RegisterJson json);
}