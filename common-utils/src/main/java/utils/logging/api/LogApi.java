package utils.logging.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import utils.logging.model.LogTask;

import java.util.List;

public interface LogApi {

  @POST("/log")
  Call<Void> sendLog(@Body List<LogTask> logTasks);
}
