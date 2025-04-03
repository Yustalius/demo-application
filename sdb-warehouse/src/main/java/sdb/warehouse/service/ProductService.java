package sdb.warehouse.service;


import sdb.warehouse.model.product.ProductDTO;

import java.util.List;

public interface ProductService {

  ProductDTO create(ProductDTO product);

  ProductDTO update(int id, ProductDTO updatedProduct);

  ProductDTO addProductQuantity(int id, int quantity);

  ProductDTO getById(int id);

  ProductDTO getByExternalId(int id);

  List<ProductDTO> getAll();

  void delete(int id);
}
