package sdb.test;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductDTO;
import sdb.service.ProductClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
  private final Faker faker = new Faker();
  private final ProductClient productClient = ProductClient.getInstance();

  @Test
  void createProductTest() {
    ProductDTO product = new ProductDTO(
        null,
        String.join(" ", faker.beer().name(), faker.color().name(), faker.food().ingredient()),
        faker.color().name(),
        faker.number().numberBetween(100, 1000),
        null
    );
    ProductDTO createdProduct = productClient.addProduct(product);

    assertThat(createdProduct.productName()).isEqualTo(product.productName());
    assertThat(createdProduct.description()).isEqualTo(product.description());
    assertThat(createdProduct.price()).isEqualTo(product.price());
  }

  @Test
  @Product
  void updateProductTest(ProductDTO product) {
    ProductDTO newProduct = new ProductDTO(
        product.id(),
        faker.beer().name(),
        faker.color().name(),
        faker.number().numberBetween(100, 1000),
        true
    );

    ProductDTO actual = productClient.updateProduct(product.id(), newProduct);
    assertThat(actual).isEqualTo(newProduct);
  }

  @Test
  @Product
  void getProductByIdTest(ProductDTO product) {
    ProductDTO productById = productClient.getProductById(product.id());

    assertThat(productById).isEqualTo(product);
  }

  @Test
  @Product
  void getAllProductsTest(ProductDTO product) {
    List<ProductDTO> productById = productClient.getAllProducts();

    assertThat(productById).isNotEmpty();
  }

  @Test
  @Product
  void deleteProductTest(ProductDTO product) {
    productClient.deleteProduct(product.id());

    assertThatThrownBy(() -> productClient.getProductById(product.id())).isInstanceOfAny(AssertionFailedError.class, RuntimeException.class);
  }
}
