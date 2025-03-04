package sdb.service.impl;

import sdb.data.dao.ProductDao;
import sdb.data.dao.impl.ProductDaoImpl;
import sdb.data.entity.products.ProductEntity;
import sdb.model.product.ProductDTO;
import sdb.service.ProductClient;

import java.util.List;

public class ProductDbClient implements ProductClient {
  private final ProductDao productDao = new ProductDaoImpl();

  @Override
  public ProductDTO addProduct(ProductDTO product) {
    return ProductDTO.fromEntity(
        productDao.create(ProductEntity.fromDTO(product)));
  }

  @Override
  public ProductDTO updateProduct(int id, ProductDTO update) {
    productDao.update(id, ProductEntity.fromDTO(update));
    return productDao.get(id).map(ProductDTO::fromEntity).
        orElseThrow(() -> new RuntimeException());
  }

  @Override
  public ProductDTO getProductById(int id) {
    return productDao.get(id).map(ProductDTO::fromEntity)
            .orElseThrow(() -> new RuntimeException());
  }

  @Override
  public List<ProductDTO> getAllProducts() {
    return productDao.getAll().orElseThrow()
        .stream()
        .map(ProductDTO::fromEntity).toList();
  }

  @Override
  public void deleteProduct(int productId) {
    productDao.delete(productId);
  }
}
