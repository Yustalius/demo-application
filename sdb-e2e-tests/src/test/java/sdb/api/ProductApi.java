package sdb.api;

import retrofit2.Call;
import retrofit2.http.*;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductSync;

import java.util.List;

public interface ProductApi {

  @POST("product/add")
  Call<ProductCoreDTO> create(@Body ProductCoreDTO product);

  @PATCH("product/{id}")
  Call<ProductCoreDTO> update(@Path("id") int productId, @Body ProductCoreDTO product);

  @GET("product/{id}")
  Call<ProductCoreDTO> getById(@Path("id") int productId);

  @GET("product")
  Call<List<ProductCoreDTO>> getAll();

  @DELETE("product/{id}")
  Call<Void> delete(@Path("id") int productId);

  @POST("product/sync")
  Call<ProductSync> sync();

}
