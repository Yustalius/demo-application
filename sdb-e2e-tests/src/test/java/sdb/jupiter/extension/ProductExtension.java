package sdb.jupiter.extension;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import sdb.data.dao.ProductCoreDao;
import sdb.data.dao.WhProductDao;
import sdb.data.dao.impl.ProductCoreDaoImpl;
import sdb.data.dao.impl.WhProductDaoImpl;
import sdb.data.entity.products.ProductCoreEntity;
import sdb.jupiter.annotation.Product;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductWhDTO;
import sdb.service.CoreProductClient;
import sdb.service.WhProductClient;
import sdb.service.impl.CoreProductDbClient;

import java.util.Random;
import java.util.UUID;

public class ProductExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
  public static final Namespace NAMESPACE = Namespace.create(ProductExtension.class);

  private final ThreadLocal<Faker> faker = ThreadLocal.withInitial(
      () -> new Faker(new Random())
  );
  private final CoreProductClient coreProductClient = new CoreProductDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Product.class)
        .ifPresent(productAnno -> {
          faker.get().number().numberBetween(1, 2);

          String productName = productAnno.productName().isEmpty()
              ? UUID.randomUUID().toString()
              : productAnno.productName();
          String description = productAnno.description().isEmpty() ? faker.get().lorem().sentence() : productAnno.description();
          ProductCoreDTO product = coreProductClient.add(new ProductCoreDTO(
              null,
              productName,
              description,
              productAnno.price() < 0 ? faker.get().number().numberBetween(100, 1000) : productAnno.price(),
              productAnno.isAvailable()
          ));

          if (productAnno.addToWarehouse() >= 0) {
            WhProductClient productWhClient = WhProductClient.getInstance();
            productWhClient.add(ProductWhDTO.fromCoreDto(product, productAnno.addToWarehouse()));
          }

          context.getStore(NAMESPACE).put(
              context.getUniqueId() + "_" + context.getRequiredTestMethod().getName(),
              product
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
  public void afterEach(ExtensionContext extensionContext) {
    ProductCoreDao productDao = new ProductCoreDaoImpl();
    WhProductDao whProductDao = new WhProductDaoImpl();

    ProductCoreEntity product = ProductCoreEntity.fromDTO(
        extensionContext.getStore(NAMESPACE).get(
            extensionContext.getUniqueId() + "_" + extensionContext.getRequiredTestMethod().getName(),
            ProductCoreDTO.class
        ));
    productDao.get(product.getId()).ifPresent(productEntity -> productDao.delete(productEntity.getId()));

    whProductDao.getByExternalId(product.getId()).ifPresent(productWhEntity -> whProductDao.delete(productWhEntity.getId()));
  }
}
