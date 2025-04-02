package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.service.ProductClient;
import sdb.service.impl.CoreProductApiClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
  private final Faker faker = new Faker();
  private final ProductClient<ProductCoreDTO> productClient = new CoreProductApiClient();

  @Test
  void createProductTest() {
    ProductCoreDTO product = new ProductCoreDTO(
        null,
        String.join(" ", faker.beer().name(), faker.color().name(), faker.food().ingredient()),
        faker.color().name(),
        faker.number().numberBetween(100, 1000),
        null
    );
    ProductCoreDTO createdProduct = productClient.add(product);

    assertThat(createdProduct.productName()).isEqualTo(product.productName());
    assertThat(createdProduct.description()).isEqualTo(product.description());
    assertThat(createdProduct.price()).isEqualTo(product.price());
  }

  @Test
  @Product
  void updateProductTest(ProductCoreDTO product) {
    ProductCoreDTO newProduct = new ProductCoreDTO(
        product.id(),
        faker.beer().name(),
        faker.color().name(),
        faker.number().numberBetween(100, 1000),
        true
    );

    ProductCoreDTO actual = productClient.update(product.id(), newProduct);
    assertThat(actual).isEqualTo(newProduct);
  }

  @Test
  @Product
  void getProductByIdTest(ProductCoreDTO product) {
    ProductCoreDTO productById = productClient.getById(product.id());

    assertThat(productById).isEqualTo(product);
  }

  @Test
  @Product
  void getAllProductsTest(ProductCoreDTO product) {
    List<ProductCoreDTO> productById = productClient.get();

    assertThat(productById).isNotEmpty();
  }

  @Test
  @Product
  void deleteProductTest(ProductCoreDTO product) {
    productClient.delete(product.id());

    assertThatThrownBy(() -> productClient.getById(product.id())).isInstanceOfAny(AssertionFailedError.class, RuntimeException.class);
  }
}
