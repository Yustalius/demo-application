package sdb.core.utils.warehouse;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sdb.core.model.product.ProductDTO;
import sdb.core.model.product.ProductWh;
import utils.logging.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class WhApiClient {
  private WhApi whApi;

  @Autowired
  private Logger logger;

  public WhApiClient(@Value("${app.warehouse.url}") String warehouseUrl) {
    OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(warehouseUrl)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();

    whApi = retrofit.create(WhApi.class);
  }

  public List<ProductWh> getProductsFromWh() {
    try {
      Response<List<ProductWh>> response = whApi.getProducts().execute();
      assert(response.isSuccessful());
      return response.body();
    } catch (Exception e) {
      logger.error("Error while getting products from warehouse: ", e);
      throw new RuntimeException("Error while getting products from warehouse", e);
    }
  }
}
