package sdb.service.impl;

import sdb.data.dao.ProductWhDao;
import sdb.data.dao.impl.ProductWhDaoImpl;
import sdb.data.entity.products.ProductWhEntity;
import sdb.model.product.ProductWhDTO;
import sdb.model.product.ProductWhDTO;
import sdb.service.ProductClient;

import java.util.List;

public class WhProductDbClient implements ProductClient<ProductWhDTO> {
  private final ProductWhDao productDao = new ProductWhDaoImpl();

  @Override
  public ProductWhDTO add(ProductWhDTO product) {
    return ProductWhDTO.fromEntity(
        productDao.create(ProductWhEntity.fromDTO(product)));
  }

  @Override
  public ProductWhDTO update(int id, ProductWhDTO update) {
    productDao.update(id, ProductWhEntity.fromDTO(update));
    return productDao.get(id).map(ProductWhDTO::fromEntity).
        orElseThrow(() -> new RuntimeException());
  }

  @Override
  public ProductWhDTO getById(int id) {
    return productDao.get(id).map(ProductWhDTO::fromEntity)
            .orElseThrow(() -> new RuntimeException());
  }

  @Override
  public List<ProductWhDTO> get() {
    return productDao.getAll().orElseThrow()
        .stream()
        .map(ProductWhDTO::fromEntity).toList();
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
