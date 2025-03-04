package sdb.api;

import retrofit2.Call;
import retrofit2.http.*;
import sdb.model.product.ProductDTO;

import java.util.List;

public interface ProductApi {

  @POST("product/add")
  Call<ProductDTO> create(@Body ProductDTO product);

  @PATCH("product/{id}")
  Call<ProductDTO> update(@Path("id") int productId, @Body ProductDTO product);

  @GET("product/{id}")
  Call<ProductDTO> getById(@Path("id") int productId);

  @GET("product")
  Call<List<ProductDTO>> getAll();

  @DELETE("product/{id}")
  Call<Void> delete(@Path("id") int productId);

}
