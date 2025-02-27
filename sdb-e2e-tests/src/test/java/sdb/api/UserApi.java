package sdb.api;

import retrofit2.Call;
import retrofit2.http.*;
import sdb.model.user.UserJson;

import java.util.List;

public interface UserApi {
  @GET("user")
  Call<List<UserJson>> getAllUsers();

  @GET("user/{id}")
  Call<UserJson> getUser(@Path("id") int id);

  @DELETE("user/{id}")
  Call<Void> deleteUser(@Path("id") int id);

  @PATCH("user/{id}")
  Call<UserJson> updateUser(
      @Path("id") int id,
      @Body UserJson user
  );
}