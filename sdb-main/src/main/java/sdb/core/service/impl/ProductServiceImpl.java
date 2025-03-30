package sdb.core.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.data.entity.product.ProductEntity;
import sdb.core.data.repository.ProductRepository;
import sdb.core.model.product.CreateProductDTO;
import sdb.core.model.product.ProductWh;
import sdb.core.service.ProductService;
import sdb.core.utils.warehouse.WhApiClient;
import utils.ex.ProductNotFoundException;
import sdb.core.model.product.ProductDTO;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private WhApiClient whApiClient;

  @PersistenceContext
  private EntityManager entityManager;

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

  @Override
  @Transactional
  public void sync() {
    // Получаем все продукты из бд
    List<ProductEntity> allProducts = productRepository.findAll();
    // Получаем все доступные продукты из склада
    List<ProductWh> availableProducts = whApiClient.getProductsFromWh();
    // Собираем все ID продуктов в бд
    List<Integer> allProductIds = allProducts.stream()
        .map(ProductEntity::getId)
        .toList();

    // Синхронизация статуса доступности продуктов
    allProducts.forEach(product -> {
      availableProducts.stream()
          .filter(productWh -> productWh.id().equals(product.getId()))
          .findFirst()
          .ifPresentOrElse(productWh -> {
            // Проверяем и обновляем статус доступности продукта
            boolean isAvailable = productWh.stockQuantity() > 0;
            if (product.getIsAvailable() != isAvailable && product.getPrice() > 0) {
              product.setIsAvailable(isAvailable);
              productRepository.save(product);
            }
          }, () -> {
            // Если продукт отсутствует на складе, обновляем его статус на недоступный
            if (product.getIsAvailable()) {
              product.setIsAvailable(false);
              productRepository.save(product);
            }
          });
    });

    // Добавление новых продуктов из склада, которых нет в локальном репозитории
    availableProducts.stream()
        .filter(productWh -> !allProductIds.contains(productWh.id()))
        .forEach(productWh -> {
          entityManager.createNativeQuery(
                  "INSERT INTO products (id, product_name, price, is_available) VALUES (?, ?, ?, ?)")
              .setParameter(1, productWh.id())
              .setParameter(2, productWh.name())
              .setParameter(3, 0)
              .setParameter(4, false)
              .executeUpdate();
        });
  }
}
