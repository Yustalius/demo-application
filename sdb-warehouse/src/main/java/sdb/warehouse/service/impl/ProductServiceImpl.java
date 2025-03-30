package sdb.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdb.warehouse.data.repository.ProductRepository;
import sdb.warehouse.model.product.ProductDTO;
import sdb.warehouse.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;

  @Override
  public ProductDTO create(ProductDTO product) {
    return null;
  }

  @Override
  public ProductDTO update(int id, ProductDTO updatedProduct) {
    return null;
  }

  @Override
  public ProductDTO getById(int productId) {
    return null;
  }

  @Override
  public List<ProductDTO> getAll() {
    return productRepository.findAll().stream()
        .map(ProductDTO::fromEntity)
        .toList();
  }

  @Override
  public void delete(int productId) {

  }
}
