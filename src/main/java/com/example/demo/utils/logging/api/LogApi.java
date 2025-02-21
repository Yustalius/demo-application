package com.example.demo.utils.logging.api;

import com.example.demo.utils.logging.model.Log;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogApi {

  @POST("/log")
  Call<Void> sendLog(@Body Log logEvent);
}
