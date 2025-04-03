package sdb.service.impl;

import sdb.data.dao.WhProductDao;
import sdb.data.dao.impl.WhProductDaoImpl;
import sdb.data.entity.products.ProductWhEntity;
import sdb.model.product.ProductWhDTO;
import sdb.service.WhProductClient;

import java.util.List;

public class WhProductDbClient implements WhProductClient {
  private final WhProductDao productDao = new WhProductDaoImpl();

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
  public ProductWhDTO updateStockQuantity(int id, int stockQuantity) {
    return productDao.get(id).map(product -> {
          product.setStockQuantity(stockQuantity);
          productDao.update(id, product);
          return ProductWhDTO.fromEntity(productDao.get(id).orElseThrow());
        })
        .orElseThrow(() -> new RuntimeException("Product with id = " + id + " not found"));
  }

  @Override
  public ProductWhDTO getById(int id) {
    return productDao.get(id).map(ProductWhDTO::fromEntity)
        .orElseThrow(() -> new RuntimeException());
  }

  @Override
  public ProductWhDTO getByExternalId(int id) {
    return productDao.getByExternalId(id).map(ProductWhDTO::fromEntity)
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
}
