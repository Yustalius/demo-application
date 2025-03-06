package sdb.api.core;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sdb.api.AuthApi;
import sdb.model.auth.LoginRequest;
import sdb.model.auth.Token;

import java.io.IOException;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class AuthInterceptor implements Interceptor {
  @NotNull
  @Override
  public Response intercept(Chain chain) throws IOException {
    OkHttpClient authClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
        .build();

    Retrofit authRetrofit = new Retrofit.Builder()
        .baseUrl(chain.request().url().scheme() + "://" +
            chain.request().url().host() + ":" +
            chain.request().url().port())
        .client(authClient)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    AuthApi authApi = authRetrofit.create(AuthApi.class);
    retrofit2.Response<Token> authResponse = authApi.login(new LoginRequest("username28", "qwerty")).execute();

    if (!authResponse.isSuccessful()) {
      throw new IOException("Authentication failed: " + authResponse.code());
    }

    String token = authResponse.body().token();

    Request originalRequest = chain.request();
    Request authenticatedRequest = originalRequest.newBuilder()
        .header("Authorization", "Bearer " + token)
        .build();

    return chain.proceed(authenticatedRequest);
  }
}
