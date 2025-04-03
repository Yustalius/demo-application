package sdb.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.warehouse.data.repository.ProductRepository;
import sdb.warehouse.model.product.ProductDTO;
import sdb.warehouse.service.ProductService;
import utils.ex.ProductNotFoundException;

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

  /**
   * Добавляет количество товара на склад. Метод позволяет добавлять как положительное, так и отрицательное количество.
   *
   * @param productId ID товара, к которому нужно добавить количество
   * @param quantity количество, которое нужно добавить к текущему запасу товара.
   *                  Положительное значение увеличивает запас, отрицательное - уменьшает.
   * @throws ProductNotFoundException если товар с указанным ID не найден
   */
  @Transactional
  @Override
  public ProductDTO addProductQuantity(int productId, int quantity) {
    return ProductDTO.fromEntity(
        productRepository.findById(productId).map(product -> {
          product.setStockQuantity(product.getStockQuantity() + quantity);
          productRepository.save(product);
          return product;
        }).orElseThrow(() -> new ProductNotFoundException(
            "Error during adding quantity to product",
            productId)));
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDTO getById(int id) {
    return productRepository.findById(id)
        .map(ProductDTO::fromEntity)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDTO getByExternalId(int id) {
    return productRepository.findByExternalProductId(id)
        .map(ProductDTO::fromEntity)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductDTO> getAll() {
    return productRepository.findAll().stream()
        .map(ProductDTO::fromEntity)
        .toList();
  }

  @Override
  public void delete(int id) {
    productRepository.findById(id)
        .map(product -> {
          productRepository.delete(product);
          return product;
        })
        .orElseThrow(() -> new ProductNotFoundException(id));
  }
}
