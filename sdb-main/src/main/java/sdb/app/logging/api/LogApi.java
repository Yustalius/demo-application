package sdb.app.logging.api;

import sdb.app.logging.model.Log;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogApi {

  @POST("/log")
  Call<Void> sendLog(@Body Log logEvent);
}
