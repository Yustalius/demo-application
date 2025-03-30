package sdb.core.service;

import sdb.core.model.product.CreateProductDTO;
import sdb.core.model.product.ProductDTO;

import java.util.List;

public interface ProductService {

  ProductDTO create(CreateProductDTO product);

  ProductDTO update(int id, CreateProductDTO updatedProduct);

  ProductDTO getById(int productId);

  List<ProductDTO> getAll();

  void delete(int productId);

  void sync();
}
