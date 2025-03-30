package sdb.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.data.entity.product.ProductEntity;
import sdb.core.data.repository.ProductRepository;
import sdb.core.model.product.CreateProductDTO;
import sdb.core.service.ProductService;
import utils.ex.ProductNotFoundException;
import sdb.core.model.product.ProductDTO;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Transactional
  public ProductDTO create(CreateProductDTO product) {
    return ProductDTO.fromEntity(
        productRepository.save(ProductEntity.fromCreateProductDTO(product)));
  }

  @Transactional
  public ProductDTO update(int id, CreateProductDTO updatedProduct) {
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
    ).orElseThrow(() -> new ProductNotFoundException("Error while updating product", id));
  }

  @Transactional(readOnly = true)
  public ProductDTO getById(int productId) {
    return ProductDTO.fromEntity(
        productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Error while getting product", productId))
    );
  }

  @Transactional(readOnly = true)
  public List<ProductDTO> getAll() {
    return productRepository.findAll().stream()
        .map(ProductDTO::fromEntity)
        .toList();
  }

  @Transactional
  public void delete(int productId) {
    productRepository.findById(productId)
        .map(product -> {
          productRepository.delete(product);
          return product;
        })
        .orElseThrow(() -> new ProductNotFoundException("Error while deleting product", productId));
  }
}
