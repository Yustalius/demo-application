package sdb.test;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductSync;
import sdb.model.product.ProductWhDTO;
import sdb.service.CoreProductClient;
import sdb.service.WhProductClient;
import sdb.service.impl.CoreProductApiClient;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static sdb.utils.RandomUtils.randomCoreProduct;
import static sdb.utils.RandomUtils.randomWhProduct;

@Isolated
public class SyncProductsTest {
  private final CoreProductApiClient coreApiClient = new CoreProductApiClient();
  private final CoreProductClient coreProductClient = CoreProductClient.getInstance();
  private final WhProductClient whProductClient = WhProductClient.getInstance();

  @Test
  void syncProductsTest() {
    Set<Integer> initialWhProducts = getProductsFromWh();
    initialWhProducts.removeAll(getProductsFromCore());

    if (initialWhProducts.isEmpty()) {
      whProductClient.add(randomWhProduct());
    }

    syncProducts();
    assertSyncResult();
  }

  @Test
  @Product(isAvailable = false, addToWarehouse = 10)
  void syncProductAvailability(ProductCoreDTO product) {
    syncProducts();
    assertThat(coreProductClient.getById(product.id()).isAvailable()).isEqualTo(true);

    whProductClient.updateStockQuantity(
        whProductClient.getByExternalId(product.id()).id(),
        0
    );

    syncProducts();
    assertThat(coreProductClient.getById(product.id()).isAvailable()).isEqualTo(false);
  }

  @Test
  void syncProductNotExistingInCore() {
    ProductWhDTO product = whProductClient.add(randomWhProduct());

    syncProducts();

    ProductCoreDTO syncedProduct = coreProductClient.getById(product.externalProductId());
    assertThat(syncedProduct)
        .isNotNull()
        .satisfies(p -> {
          assertThat(p.productName()).isEqualTo(product.name());
          assertThat(p.price()).isZero();
          assertThat(p.isAvailable()).isFalse();
        });
  }

  @Test
  @Product(price = 0, isAvailable = false, addToWarehouse = 100)
  void productWithoutPriceNotAvailable(ProductCoreDTO product) {
    syncProducts();

    assertThat(coreProductClient.getById(product.id()).isAvailable()).isFalse();
  }

  @Test
  @Product(price = 0, isAvailable = false, addToWarehouse = 100)
  void productBecomesAvailableAfterSettingPrice(ProductCoreDTO product) {
    syncProducts();
    assertThat(coreProductClient.getById(product.id()).isAvailable()).isFalse();

    coreProductClient.update(
        product.id(),
        randomCoreProduct()
    );
    syncProducts();

    assertThat(coreProductClient.getById(product.id()).isAvailable()).isTrue();
  }

  @Test
  @Product(isAvailable = false)
  void syncShouldNotChangeExistingProductName(ProductCoreDTO product) {
    whProductClient.add(new ProductWhDTO(product.id(), UUID.randomUUID().toString(), 100));
    ProductSync productSync = syncProducts();
    assertThat(productSync.updatedProducts()).isPositive();

    assertThat(coreProductClient.getById(product.id()).productName()).isEqualTo(product.productName());
  }

  private ProductSync syncProducts() {
    return coreApiClient.sync();
  }

  @NotNull
  private Set<Integer> getProductsFromCore() {
    return coreProductClient.get().stream()
        .map(ProductCoreDTO::id)
        .collect(Collectors.toSet());
  }

  @NotNull
  private Set<Integer> getProductsFromWh() {
    return whProductClient.get().stream()
        .map(ProductWhDTO::externalProductId)
        .collect(Collectors.toSet());
  }

  private void assertSyncResult() {
    Set<Integer> updatedWhProducts = getProductsFromWh();
    updatedWhProducts.removeAll(getProductsFromCore());
    assertThat(updatedWhProducts).isEmpty();
  }
}
