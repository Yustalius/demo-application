package sdb.api.core;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sdb.config.Config;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  private final OkHttpClient okHttpClient;
  private final Retrofit retrofit;

  public RestClient(String baseUrl) {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
      LoggerFactory.getLogger("Retrofit").info(message);
    });
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
        .build();
    this.okHttpClient = okHttpClient;
    this.retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }

  @Nonnull
  public <T> T create(final Class<T> service) {
    return this.retrofit.create(service);
  }
}
