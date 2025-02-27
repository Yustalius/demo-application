package sdb.api;

import retrofit2.Call;
import retrofit2.http.*;
import sdb.model.user.UserDTO;

import java.util.List;

public interface UserApi {
  @GET("user")
  Call<List<UserDTO>> getAllUsers();

  @GET("user/{id}")
  Call<UserDTO> getUser(@Path("id") int id);

  @DELETE("user/{id}")
  Call<Void> deleteUser(@Path("id") int id);

  @PATCH("user/{id}")
  Call<UserDTO> updateUser(
      @Path("id") int id,
      @Body UserDTO user
  );
}