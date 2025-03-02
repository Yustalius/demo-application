package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.config.Config;
import sdb.data.entity.products.ProductEntity;
import sdb.data.repositrory.ProductRepository;
import sdb.data.repositrory.ProductRepositoryHibernate;

import java.util.Optional;
import java.util.PropertyResourceBundle;

import static sdb.data.Databases.*;

public class ProductTest {
  private static final Config CFG = Config.getInstance();
  ProductRepository productRepository = new ProductRepositoryHibernate();

  @Test
  void hibernateTest() {

    ProductEntity product = new ProductEntity();
    product.setProductName("APEROL");
    product.setDescription("New product");
    productRepository.create(product);
/*    Optional<ProductEntity> byId = productRepository.findById(1);
    System.out.println(byId.get().getProductName());
    Optional<ProductEntity> negroni = productRepository.findByName("NEGRONI");
    System.out.println(negroni.get().getId());*/

  }


}
