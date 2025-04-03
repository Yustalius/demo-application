package sdb.service;

import retrofit2.http.Path;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductSync;
import sdb.service.impl.CoreProductDbClient;

import java.util.List;

public interface CoreProductClient {

  static CoreProductClient getInstance() {
    return new CoreProductDbClient();
  }

  ProductCoreDTO add(ProductCoreDTO product);

  ProductCoreDTO update(int id, ProductCoreDTO product);

  ProductCoreDTO getById(@Path("id") int productId);

  List<ProductCoreDTO> get();

  void delete(int productId);

  ProductSync sync();
}
