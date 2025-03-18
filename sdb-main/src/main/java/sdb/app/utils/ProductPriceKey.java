package sdb.app.utils;

import lombok.Data;

import java.util.Objects;

/**
 * Класс для группировки продуктов по ID и цене
 */
@Data
public class ProductPriceKey {
  private final int productId;
  private final int price;
  
  public ProductPriceKey(int productId, int price) {
    this.productId = productId;
    this.price = price;
  }
  
  public int getProductId() {
    return productId;
  }
  
  public int getPrice() {
    return price;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductPriceKey that = (ProductPriceKey) o;
    return productId == that.productId && 
           Double.compare(price, that.price) == 0;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(productId, price);
  }
} 