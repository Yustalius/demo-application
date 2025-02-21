package com.example.demo.utils.logging.api;

import com.example.demo.utils.logging.model.Log;
import com.example.demo.utils.logging.model.LogLevel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class LogApiClient {
  private final LogApi logApi;

  public LogApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8082/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    logApi = retrofit.create(LogApi.class);
  }

  public void sendLog(String timestamp, LogLevel logLevel, String path, String message) {
    Log logRequest = new Log(timestamp, logLevel, path, message);

    Call<Void> call = logApi.sendLog(logRequest);
    try {
      call.execute();
    } catch (IOException e) {
      System.err.println("Failed to send log: " + e.getMessage());
    }
  }
}
