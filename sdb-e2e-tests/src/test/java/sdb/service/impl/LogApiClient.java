package sdb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sdb.api.AuthApi;
import sdb.api.LogApi;
import sdb.api.core.RestClient;
import sdb.model.log.LogTask;

import java.io.IOException;
import java.util.List;

public class LogApiClient extends RestClient {
  private final LogApi logApi;
  private static final Logger logger = LoggerFactory.getLogger(LogApiClient.class);

  public LogApiClient() {
    super(CFG.loggingUrl());
    this.logApi = create(LogApi.class);
  }

  public void sendLog(List<LogTask> logRequests) {
    try {
      logApi.sendLog(logRequests).execute();
    } catch (IOException e) {
      logger.error("Failed to send log: {}", e.getMessage());
    }
  }
}
