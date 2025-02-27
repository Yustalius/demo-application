package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Purchase;
import sdb.jupiter.annotation.User;
import sdb.model.product.Products;
import sdb.model.product.PurchaseJson;
import sdb.model.user.UserDTO;
import sdb.service.PurchaseClient;
import sdb.service.UserClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseTest {

  private final PurchaseClient purchaseClient = PurchaseClient.getInstance();

  @Test
  @User
  void addPurchaseTest(UserDTO user) {
    purchaseClient.createPurchase(new PurchaseJson(null, user.id(), Products.LONG_ISLAND, 200));

    PurchaseJson createdPurchase = purchaseClient.getUserPurchases(user.id()).get(0);
    assertThat(createdPurchase.purchaseId()).isNotNull();
    assertThat(createdPurchase.productName()).isEqualTo(Products.LONG_ISLAND);
  }

  @Test
  void getAllPurchases() {
    assertThat(purchaseClient.getPurchases()).isNotEmpty();
  }

  @Test
  @User(
      purchases = @Purchase(
          product = Products.NEGRONI,
          price = 200
      )
  )
  void getPurchaseTest(UserDTO user) {
    List<PurchaseJson> purchases = purchaseClient.getUserPurchases(user.id());
    PurchaseJson randomPurchase = purchases.stream()
        .findAny()
        .orElseThrow();

    PurchaseJson purchase = purchaseClient.getPurchase(randomPurchase.purchaseId());
    assertThat(purchase.purchaseId()).isEqualTo(randomPurchase.purchaseId());
  }

  @Test
  @User(
      purchases = @Purchase(
          product = Products.APEROL,
          price = 200
      )
  )
  void getUserPurchasesTest(UserDTO user) {
    List<PurchaseJson> userPurchases = purchaseClient.getUserPurchases(user.id());

    assertThat(userPurchases).isNotEmpty();
  }
}
