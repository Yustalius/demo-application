package sdb.service.impl;

import retrofit2.Response;
import sdb.api.OrderApi;
import sdb.api.core.AuthInterceptor;
import sdb.api.core.RestClient;
import sdb.model.order.OrderDTO;
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
  public List<OrderDTO> createOrder(OrderDTO... purchases) {
    Response<List<OrderDTO>> response;
    try {
      response = orderApi.addOrders(purchases).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<OrderDTO> getOrders() {
    Response<List<OrderDTO>> response;
    try {
      response = orderApi.getAllOrders().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public OrderDTO getOrder(int purchaseId) {
    Response<OrderDTO> response;
    try {
      response = orderApi.getOrder(purchaseId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<OrderDTO> getUserOrders(int userId) {
    Response<List<OrderDTO>> response;
    try {
      response = orderApi.getUserOrders(userId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
