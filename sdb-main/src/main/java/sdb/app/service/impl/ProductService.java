package sdb.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.app.data.entity.product.ProductEntity;
import sdb.app.data.repository.ProductRepository;
import sdb.app.ex.ProductNotFoundException;
import sdb.app.model.product.ProductDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Transactional
  public ProductDTO create(ProductDTO product) {
    return ProductDTO.fromEntity(
        productRepository.save(ProductEntity.fromDTO(product)));
  }

  @Transactional
  public ProductDTO update(int id, ProductDTO updatedProduct) {
    return productRepository.findById(id).map(product -> {
          if (updatedProduct.productName() != null) {
            product.setProductName(updatedProduct.productName());
          }
          if (updatedProduct.description() != null) {
            product.setDescription(updatedProduct.description());
          }
          if (updatedProduct.price() != null) {
            product.setPrice(updatedProduct.price());
          }

          return ProductDTO.fromEntity(productRepository.save(product));
        }
    ).orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Transactional(readOnly = true)
  public ProductEntity getById(int productId) {
    return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
  }

  @Transactional(readOnly = true)
  public List<ProductEntity> getAll() {
    return productRepository.findAll();
  }

  @Transactional
  public void delete(int productId) {
    productRepository.findById(productId).ifPresent(product -> {
      productRepository.deleteById(productId);
    });
  }
}
