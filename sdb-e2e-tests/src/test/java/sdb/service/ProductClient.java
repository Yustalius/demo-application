package sdb.service;

import retrofit2.http.Path;
import sdb.model.Services;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductDTO;
import sdb.model.product.ProductWhDTO;
import sdb.service.impl.CoreProductDbClient;
import sdb.service.impl.WhProductDbClient;

import java.util.List;

public interface ProductClient<T extends ProductDTO> {

  @SuppressWarnings("unchecked")
  static <T extends ProductDTO> ProductClient<T> getInstance(Services services) {
    return switch (services) {
      case CORE -> (ProductClient<T>) new CoreProductDbClient();
      case WAREHOUSE -> (ProductClient<T>) new WhProductDbClient();
      default -> throw new IllegalArgumentException("Unknown service type");
    };
  }

  static ProductClient<ProductCoreDTO> getInstance(Services services, ProductCoreDTO type) {
    return getInstance(services);
  }

  static ProductClient<ProductWhDTO> getInstance(Services services, ProductWhDTO type) {
    return getInstance(services);
  }

  T add(T product);

  T update(int id, T product);

  T getById(@Path("id") int productId);

  List<T> get();

  void delete(int productId);

  void sync();
}
