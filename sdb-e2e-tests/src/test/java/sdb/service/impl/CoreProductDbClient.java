package sdb.service.impl;

import sdb.data.dao.ProductСoreDao;
import sdb.data.dao.impl.ProductCoreDaoImpl;
import sdb.data.entity.products.ProductCoreEntity;
import sdb.model.product.ProductCoreDTO;
import sdb.service.ProductClient;

import java.util.List;

public class CoreProductDbClient implements ProductClient<ProductCoreDTO> {
  private final ProductСoreDao productDao = new ProductCoreDaoImpl();

  @Override
  public ProductCoreDTO add(ProductCoreDTO product) {
    return ProductCoreDTO.fromEntity(
        productDao.create(ProductCoreEntity.fromDTO(product)));
  }

  @Override
  public ProductCoreDTO update(int id, ProductCoreDTO update) {
    productDao.update(id, ProductCoreEntity.fromDTO(update));
    return productDao.get(id).map(ProductCoreDTO::fromEntity).
        orElseThrow(() -> new RuntimeException());
  }

  @Override
  public ProductCoreDTO getById(int id) {
    return productDao.get(id).map(ProductCoreDTO::fromEntity)
            .orElseThrow(() -> new RuntimeException());
  }

  @Override
  public List<ProductCoreDTO> get() {
    return productDao.getAll().orElseThrow()
        .stream()
        .map(ProductCoreDTO::fromEntity).toList();
  }

  @Override
  public void delete(int productId) {
    productDao.delete(productId);
  }

  @Deprecated
  @Override
  public void sync() {
    System.out.println("Method not realized");
  }
}
