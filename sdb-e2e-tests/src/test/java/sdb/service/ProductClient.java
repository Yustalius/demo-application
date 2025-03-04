package sdb.service;

import retrofit2.http.Path;
import sdb.model.product.ProductDTO;
import sdb.service.impl.ProductApiClient;

import java.util.List;

public interface ProductClient {

  // TODO Dependency Injection
  static ProductClient getInstance() {
    return new ProductApiClient();
  }

  ProductDTO addProduct(ProductDTO product);

  ProductDTO updateProduct(int id, ProductDTO product);

  ProductDTO getProductById(@Path("id") int productId);

  List<ProductDTO> getAllProducts();

  void deleteProduct(int productId);
}
