package sdb.service;

import retrofit2.http.Path;
import sdb.model.Services;
import sdb.model.product.ProductDTO;
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

  T add(T product);

  T update(int id, T product);

  T getById(@Path("id") int productId);

  List<T> get();

  void delete(int productId);

  void sync();
}
