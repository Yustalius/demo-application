package sdb.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderStatusDTO;

import java.util.List;

public interface OrderApi {

  @POST("order/add")
  Call<OrderDTO> add(@Body OrderDTO order);

  @POST("order/{id}/status")
  Call<OrderDTO> updateStatus(@Path("id") int orderId, @Body OrderStatusDTO status);

  @GET("order/user/{id}")
  Call<List<OrderDTO>> getByUserId(@Path("id") int userId);

  @GET("order/{id}")
  Call<OrderDTO> getById(@Path("id") int orderId);

  @GET("order")
  Call<List<OrderDTO>> get();
}