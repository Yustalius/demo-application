package sdb.api;

import retrofit2.Call;
import retrofit2.http.*;
import sdb.model.product.PurchaseJson;

import java.util.List;

public interface PurchaseApi {

  @POST("purchase/add")
  Call<Void> addPurchases(@Body PurchaseJson... purchaseJsons);

  @GET("purchase/user/{id}")
  Call<List<PurchaseJson>> getUserPurchases(@Path("id") int userId);

  @GET("purchase/{id}")
  Call<PurchaseJson> getPurchase(@Path("id") int purchaseId);

  @GET("purchase")
  Call<List<PurchaseJson>> getAllPurchases();
}