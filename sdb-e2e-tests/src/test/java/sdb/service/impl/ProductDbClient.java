package sdb.service.impl;

public class ProductDbClient {

/*  private final ProductRepository productRepository;

  // CREATE
  public ProductEntity createProduct(ProductEntity product) {
    return productRepository.save(product);
  }

  // READ
  public List<ProductEntity> getAllProducts() {
    return productRepository.findAll();
  }

  public Optional<ProductEntity> getProductById(Integer id) {
    return productRepository.findById(id);
  }

  public Optional<ProductEntity> getProductByName(String name) {
    return productRepository.findByProductName(name);
  }

  // UPDATE
  public ProductEntity updateProduct(Integer id, ProductEntity updatedProduct) {
    return productRepository.findById(id)
        .map(product -> {
          product.setProductName(updatedProduct.getProductName());
          product.setDescription(updatedProduct.getDescription());
          return productRepository.save(product);
        })
        .orElseThrow(() -> new RuntimeException("Product not found"));
  }

  // DELETE
  public void deleteProduct(Integer id) {
    productRepository.deleteById(id);
  }*/
}
