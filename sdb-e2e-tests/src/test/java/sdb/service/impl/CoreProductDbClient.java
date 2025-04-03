package sdb.service.impl;

import sdb.data.dao.ProductCoreDao;
import sdb.data.dao.impl.ProductCoreDaoImpl;
import sdb.data.entity.products.ProductCoreEntity;
import sdb.model.product.ProductCoreDTO;
import sdb.model.product.ProductSync;
import sdb.service.CoreProductClient;

import java.util.List;

public class CoreProductDbClient implements CoreProductClient {
  private final ProductCoreDao productDao = new ProductCoreDaoImpl();

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
            .orElse(null);
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
  public ProductSync sync() {
    System.out.println("Method not realized");
    return null;
  }
}
