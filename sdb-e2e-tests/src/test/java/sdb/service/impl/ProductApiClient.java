package sdb.service.impl;

import org.opentest4j.AssertionFailedError;
import retrofit2.Response;
import sdb.api.ProductApi;
import sdb.api.core.AuthInterceptor;
import sdb.api.core.RestClient;
import sdb.model.product.ProductDTO;
import sdb.service.ProductClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductApiClient extends RestClient implements ProductClient {

  private final ProductApi productApi;

  public ProductApiClient() {
    super(CFG.coreUrl(), new AuthInterceptor());
    this.productApi = create(ProductApi.class);
  }

  @Override
  public ProductDTO addProduct(ProductDTO product) {
    Response<ProductDTO> response;
    try {
      response = productApi.create(product).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public ProductDTO updateProduct(int id, ProductDTO product) {
    Response<ProductDTO> response;
    try {
      response = productApi.update(id ,product).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public ProductDTO getProductById(int productId) {
    Response<ProductDTO> response;
    try {
      response = productApi.getById(productId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<ProductDTO> getAllProducts() {
    Response<List<ProductDTO>> response;
    try {
      response = productApi.getAll().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public void deleteProduct(int productId) {
    Response<Void> response;
    try {
      response = productApi.delete(productId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
  }
}
