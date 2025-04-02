package utils.logging.api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.logging.model.LogTask;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;

public class LogApiClient {
  private final LogApi logApi;
  private final Logger logger = Logger.getLogger("logger");

  public LogApiClient(String loggerUrl) {
    OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build();
        
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(loggerUrl)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    logApi = retrofit.create(LogApi.class);
  }

  public void sendLog(List<LogTask> logRequests) {
    try {
      logApi.sendLog(logRequests).execute();
    } catch (IOException e) {
      logger.log(Level.WARNING, "Failed to send log: " + e.getMessage());
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Unexpected error sending log: " + e.getMessage());
    }
  }
}
