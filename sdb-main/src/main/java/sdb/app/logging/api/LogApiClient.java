package sdb.app.logging.api;

import sdb.app.logging.model.LogTask;
import sdb.app.logging.model.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class LogApiClient {
  private final LogApi logApi;

  private static final Logger logger = LoggerFactory.getLogger(LogApiClient.class);

  public LogApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8082/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    logApi = retrofit.create(LogApi.class);
  }

  public void sendLog(LogTask logRequest) {
    try {
      logApi.sendLog(logRequest).execute();
    } catch (IOException e) {
      logger.error("Failed to send log: {}", e.getMessage());
    }
  }
}
