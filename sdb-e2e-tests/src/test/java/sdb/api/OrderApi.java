package sdb.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sdb.model.order.OrderDTO;

import java.util.List;

public interface OrderApi {

  @POST("order/add")
  Call<OrderDTO> addOrders(@Body OrderDTO order);

  @GET("order/user/{id}")
  Call<List<OrderDTO>> getUserOrders(@Path("id") int userId);

  @GET("order/{id}")
  Call<OrderDTO> getOrder(@Path("id") int orderId);

  @GET("order")
  Call<List<OrderDTO>> getAllOrders();
}