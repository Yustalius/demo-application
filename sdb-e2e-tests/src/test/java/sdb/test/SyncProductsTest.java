package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductSync;
import sdb.model.product.ProductWhDTO;
import sdb.service.CoreProductClient;
import sdb.service.WhProductClient;
import sdb.service.impl.CoreProductApiClient;

import static org.assertj.core.api.Assertions.assertThat;

@Isolated
public class SyncProductsTest {
  private static final Faker faker = new Faker();

  private final CoreProductApiClient coreApiClient = new CoreProductApiClient();
  private final CoreProductClient productCoreClient = CoreProductClient.getInstance();
  private final WhProductClient productWhClient = WhProductClient.getInstance();

  @Test
  @Product(isAvailable = false, addToWarehouse = 10)
  void syncProductAvailability(ProductCoreDTO product) {
    syncProducts();
    assertThat(productCoreClient.getById(product.id()).isAvailable()).isEqualTo(true);

    productWhClient.updateStockQuantity(
        productWhClient.getByExternalId(product.id()).id(),
        0
    );

    syncProducts();
    assertThat(productCoreClient.getById(product.id()).isAvailable()).isEqualTo(false);
  }

  @Test
  void syncProductNotExistingInCore() {
    ProductWhDTO product = productWhClient.add(new ProductWhDTO(
        faker.number().positive(),
        faker.lorem().sentence(),
        faker.number().numberBetween(10, 100))
    );

    syncProducts();

    ProductCoreDTO syncedProduct = productCoreClient.getById(product.externalProductId());
    assertThat(syncedProduct).isNotNull();
    assertThat(syncedProduct.productName()).isEqualTo(product.name());
    assertThat(syncedProduct.price()).isEqualTo(0);
    assertThat(syncedProduct.isAvailable()).isFalse();
  }

  @Test
  @Product(price = 0, isAvailable = false, addToWarehouse = 100)
  void productWithoutPriceNotAvailable(ProductCoreDTO product) {
    syncProducts();

    assertThat(productCoreClient.getById(product.id()).isAvailable()).isFalse();
  }

  @Test
  @Product(price = 0, isAvailable = false, addToWarehouse = 100)
  void productBecomesAvailableAfterSettingPrice(ProductCoreDTO product) {
    syncProducts();
    assertThat(productCoreClient.getById(product.id()).isAvailable()).isFalse();

    productCoreClient.update(
        product.id(),
        new ProductCoreDTO(
            product.productName(),
            product.description(),
            faker.number().numberBetween(100, 1000)
        )
    );
    syncProducts();

    assertThat(productCoreClient.getById(product.id()).isAvailable()).isTrue();
  }

  @Test
  @Product(isAvailable = false)
  void syncShouldNotChangeExistingProductName(ProductCoreDTO product) {
    productWhClient.add(new ProductWhDTO(product.id(), faker.lorem().sentence(), 100));
    ProductSync productSync = syncProducts();
    assertThat(productSync.updatedProducts()).isPositive();

    assertThat(productCoreClient.getById(product.id()).productName()).isEqualTo(product.productName());
  }

  private ProductSync syncProducts() {
    return coreApiClient.sync();
  }
}
