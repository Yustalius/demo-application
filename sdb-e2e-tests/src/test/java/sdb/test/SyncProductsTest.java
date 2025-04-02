package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductWhDTO;
import sdb.service.ProductClient;
import sdb.service.impl.CoreProductApiClient;

import static sdb.model.Services.CORE;
import static sdb.model.Services.WAREHOUSE;

public class SyncProductsTest {
  private static final Faker faker = new Faker();

  private final CoreProductApiClient coreApiClient = new CoreProductApiClient();
  private final ProductClient<ProductCoreDTO> productCoreClient = ProductClient.getInstance(CORE);
  private final ProductClient<ProductWhDTO> productWhClient = ProductClient.getInstance(WAREHOUSE);

  @Test
  @Product(isAvailable = false)
  void syncProductAvailabilityFromWhToCoreTest(ProductCoreDTO product) {
    /*
    * 1. Добавляем продукт в обе бд
    * 2.
    * */

    productWhClient.add(ProductWhDTO.fromCoreDto(product, faker.number().numberBetween(1, 100)));

  }
}
