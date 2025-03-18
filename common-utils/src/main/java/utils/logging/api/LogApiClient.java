package utils.logging.api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.logging.model.LogTask;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogApiClient {
  private final LogApi logApi;
  private final Logger logger = Logger.getLogger("logger");

  public LogApiClient(String loggerUrl) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(loggerUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    logApi = retrofit.create(LogApi.class);
  }

  public void sendLog(List<LogTask> logRequests) {
    try {
      logApi.sendLog(logRequests).execute();
    } catch (IOException e) {
      logger.log(Level.WARNING, "Failed to send log: " + e.getMessage());
    }
  }
}
