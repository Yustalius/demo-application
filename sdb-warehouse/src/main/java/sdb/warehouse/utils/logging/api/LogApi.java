package sdb.warehouse.utils.logging.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import sdb.warehouse.utils.logging.model.LogTask;

import java.util.List;

public interface LogApi {

  @POST("/log")
  Call<Void> sendLog(@Body List<LogTask> logTasks);
}
