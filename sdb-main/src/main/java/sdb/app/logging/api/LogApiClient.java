package sdb.app.logging.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sdb.app.config.Config;
import sdb.app.logging.model.LogTask;

import java.io.IOException;
import java.util.List;

@Component
public class LogApiClient {

  private final LogApi logApi;
  private static final Logger logger = LoggerFactory.getLogger(LogApiClient.class);

  public LogApiClient(@Value("${app.logging.url}") String loggerUrl) {
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
      logger.error("Failed to send log: {}", e.getMessage());
    }
  }
}
