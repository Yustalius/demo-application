package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.model.product.Products;
import sdb.model.product.PurchaseJson;
import sdb.model.user.UserJson;
import sdb.service.PurchaseClient;
import sdb.service.UserClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseTest {

  private final UserClient userClient = UserClient.getInstance();
  private final PurchaseClient purchaseClient = PurchaseClient.getInstance();

  @Test
  void addPurchaseTest() {

  }

  @Test
  void getAllPurchases() {
    assertThat(purchaseClient.getPurchases()).isNotEmpty();
  }

  @Test
  void getPurchaseTest() {
    List<PurchaseJson> purchases = purchaseClient.getPurchases();
    PurchaseJson randomPurchase = purchases.stream()
        .findAny()
        .orElseThrow();

    PurchaseJson purchase = purchaseClient.getPurchase(randomPurchase.purchaseId());
    assertThat(purchase.purchaseId()).isEqualTo(randomPurchase.purchaseId());
  }

  @Test
  void getUserPurchasesTest() {
    List<UserJson> allUsers = userClient.getAllUsers();
    UserJson randomUser = allUsers.stream()
        .findAny()
        .orElseThrow();
    purchaseClient.createPurchase(
        new PurchaseJson(
            null,
            randomUser.id(),
            Products.LONG_ISLAND,
            200
        ));

    List<PurchaseJson> userPurchases = purchaseClient.getUserPurchases(randomUser.id());
    assertThat(userPurchases).isNotEmpty();
  }
}
