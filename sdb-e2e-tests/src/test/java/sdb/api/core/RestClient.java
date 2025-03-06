package sdb.api.core;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
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

  public RestClient(String baseUrl, Interceptor... interceptors) {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
        message -> LoggerFactory.getLogger("ApiClient").info(message)
    );
    loggingInterceptor.setLevel(BODY);

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor);

    for (Interceptor interceptor : interceptors) {
      clientBuilder.addInterceptor(interceptor);
    }

    this.okHttpClient = clientBuilder.build();
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
