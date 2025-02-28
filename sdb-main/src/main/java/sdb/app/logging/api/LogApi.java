package sdb.app.logging.api;

import sdb.app.logging.model.LogTask;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogApi {

  @POST("/log")
  Call<Void> sendLog(@Body LogTask logEvent);
}
