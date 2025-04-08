package sdb.service.impl;

import retrofit2.Response;
import sdb.api.OrderApi;
import sdb.api.core.AuthInterceptor;
import sdb.api.core.RestClient;
import sdb.model.order.OrderDTO;
import sdb.model.order.OrderItemDTO;
import sdb.model.order.OrderStatus;
import sdb.model.order.OrderStatusDTO;
import sdb.service.OrderClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderApiClient extends RestClient implements OrderClient {

  OrderApi orderApi;

  public OrderApiClient() {
    super(CFG.coreUrl(), new AuthInterceptor());
    this.orderApi = create(OrderApi.class);
  }

  @Override
  public OrderDTO create(int userId, List<OrderItemDTO> items) {
    Response<OrderDTO> response;
    try {
      response = orderApi.add(new OrderDTO(userId, items)).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();  }

  @Override
  public OrderDTO updateStatus(int orderId, OrderStatus status) {
    Response<OrderDTO> response;
    try {
      response = orderApi.updateStatus(orderId, new OrderStatusDTO(status)).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<OrderDTO> get() {
    Response<List<OrderDTO>> response;
    try {
      response = orderApi.get().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public OrderDTO getById(int id) {
    Response<OrderDTO> response;
    try {
      response = orderApi.getById(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<OrderDTO> getByUserId(int userId) {
    Response<List<OrderDTO>> response;
    try {
      response = orderApi.getByUserId(userId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
