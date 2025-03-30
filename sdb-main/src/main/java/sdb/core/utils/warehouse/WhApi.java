package sdb.core.utils.warehouse;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;
import sdb.core.model.product.ProductDTO;
import sdb.core.model.product.ProductWh;

import java.util.List;

public interface WhApi {

  @GET("/product")
  Call<List<ProductWh>> getProducts();
}
