package sdb.jupiter.extension;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.data.dao.ProductСoreDao;
import sdb.data.dao.impl.ProductCoreDaoImpl;
import sdb.data.entity.products.ProductCoreEntity;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.service.ProductClient;
import sdb.service.impl.CoreProductDbClient;

public class ProductExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
  public static final Namespace NAMESPACE = Namespace.create(ProductExtension.class);

  private final Faker faker = new Faker();
  private final ProductClient<ProductCoreDTO> productClient = new CoreProductDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Product.class)
        .ifPresent(productAnno -> {
          faker.number().numberBetween(1, 2);

          String productName = productAnno.productName().isEmpty()
              ? String.join(" ", faker.beer().name(), faker.color().name(), faker.food().ingredient())
              : productAnno.productName();
          String description = productAnno.description().isEmpty() ? faker.lorem().sentence() : productAnno.description();
          ProductCoreDTO createdProduct = productClient.add(new ProductCoreDTO(
              null,
              productName,
              description,
              productAnno.price() == 0 ? faker.number().numberBetween(100, 1000) : productAnno.price(),
              productAnno.isAvailable()
          ));

          context.getStore(NAMESPACE).put(
              context.getUniqueId() + "_" + context.getRequiredTestMethod().getName(),
              createdProduct
          );
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(ProductCoreDTO.class);
  }

  @Override
  public ProductCoreDTO resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(
        extensionContext.getUniqueId() + "_" + extensionContext.getRequiredTestMethod().getName(),
        ProductCoreDTO.class
    );
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    ProductСoreDao productDao = new ProductCoreDaoImpl();

    ProductCoreEntity entity = ProductCoreEntity.fromDTO(
        extensionContext.getStore(NAMESPACE).get(
            extensionContext.getUniqueId() + "_" + extensionContext.getRequiredTestMethod().getName(),
            ProductCoreDTO.class
        ));
    productDao.get(entity.getId()).ifPresent(product -> productDao.delete(product.getId()));
  }
}
