package sdb.service.impl;

import retrofit2.Response;
import sdb.api.ProductApi;
import sdb.api.core.AuthInterceptor;
import sdb.api.core.RestClient;
import sdb.model.product.ProductCoreDTO;
import sdb.service.ProductClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CoreProductApiClient extends RestClient implements ProductClient<ProductCoreDTO> {

  private final ProductApi productApi;

  public CoreProductApiClient() {
    super(CFG.coreUrl(), new AuthInterceptor());
    this.productApi = create(ProductApi.class);
  }

  @Override
  public ProductCoreDTO add(ProductCoreDTO product) {
    Response<ProductCoreDTO> response;
    try {
      response = productApi.create(product).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public ProductCoreDTO update(int id, ProductCoreDTO product) {
    Response<ProductCoreDTO> response;
    try {
      response = productApi.update(id ,product).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public ProductCoreDTO getById(int productId) {
    Response<ProductCoreDTO> response;
    try {
      response = productApi.getById(productId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<ProductCoreDTO> get() {
    Response<List<ProductCoreDTO>> response;
    try {
      response = productApi.getAll().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public void delete(int productId) {
    Response<Void> response;
    try {
      response = productApi.delete(productId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
  }

  @Override
  public void sync() {
    Response<Void> response;
    try {
      response = productApi.sync().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
  }
}
