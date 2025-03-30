package sdb.warehouse.service;


import sdb.warehouse.model.product.ProductDTO;

import java.util.List;

public interface ProductService {

  ProductDTO create(ProductDTO product);

  ProductDTO update(int id, ProductDTO updatedProduct);

  ProductDTO getById(int productId);

  List<ProductDTO> getAll();

  void delete(int productId);
}
