package sdb.jupiter.extension;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.config.Config;
import sdb.data.dao.ProductDao;
import sdb.data.dao.impl.ProductDaoImpl;
import sdb.data.entity.products.ProductEntity;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductDTO;
import sdb.service.ProductClient;
import sdb.service.impl.ProductDbClient;

import static sdb.data.Databases.dataSource;

public class ProductExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ProductExtension.class);

  private final Faker faker = new Faker();
  private final ProductClient productClient = new ProductDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Product.class)
        .ifPresent(productAnno -> {

          String productName = productAnno.productName().isEmpty() 
              ? String.join(" ", faker.beer().name(), faker.color().name(), faker.food().ingredient()) 
              : productAnno.productName();
          String description = productAnno.description().isEmpty() ? faker.lorem().sentence() : productAnno.description();
          ProductDTO createdProduct = productClient.addProduct(new ProductDTO(
              null,
              productName,
              description,
              productAnno.price() == 0 ? faker.number().numberBetween(100, 1000) : productAnno.price()
          ));

          context.getStore(NAMESPACE).put(
              context.getUniqueId() + "_" + context.getRequiredTestMethod().getName(),
              createdProduct
          );
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(ProductDTO.class);
  }

  @Override
  public ProductDTO resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(
        extensionContext.getUniqueId() + "_" + extensionContext.getRequiredTestMethod().getName(),
        ProductDTO.class
    );
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    ProductDao productDao = new ProductDaoImpl();

    ProductEntity entity = ProductEntity.fromDTO(
        extensionContext.getStore(NAMESPACE).get(
            extensionContext.getUniqueId() + "_" + extensionContext.getRequiredTestMethod().getName(),
            ProductDTO.class
        ));
    productDao.get(entity.getId()).ifPresent(product -> productDao.delete(product.getId()));
  }
}
