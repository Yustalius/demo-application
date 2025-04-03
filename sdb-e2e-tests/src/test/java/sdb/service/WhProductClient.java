package sdb.service;

import sdb.model.Services;
import sdb.model.product.ProductWhDTO;
import sdb.service.impl.CoreProductDbClient;
import sdb.service.impl.WhProductDbClient;

import java.util.List;

public interface WhProductClient {

    static WhProductClient getInstance() {
        return new WhProductDbClient();
    }

    ProductWhDTO add(ProductWhDTO product);

    ProductWhDTO getById(int id);

    ProductWhDTO getByExternalId(int id);

    List<ProductWhDTO> get();

    ProductWhDTO update(int id, ProductWhDTO product);

    ProductWhDTO updateStockQuantity(int id, int stockQuantity);

    void delete(int productId);
}
