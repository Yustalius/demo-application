package sdb.test;

import org.junit.jupiter.api.Test;
import sdb.jupiter.annotation.Purchase;
import sdb.jupiter.annotation.User;
import sdb.model.product.PurchaseJson;
import sdb.model.user.UserDTO;
import sdb.service.PurchaseClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseTest {

  private final PurchaseClient purchaseClient = PurchaseClient.getInstance();
  private static final int PRODUCT_ID = 156;

  @Test
  @User
  void addPurchaseTest(UserDTO user) {
    purchaseClient.createPurchase(new PurchaseJson(null, user.id(), PRODUCT_ID, 200, null));

    PurchaseJson createdPurchase = purchaseClient.getUserPurchases(user.id()).get(0);
    assertThat(createdPurchase.purchaseId()).isNotNull();
    assertThat(createdPurchase.productId()).isEqualTo(PRODUCT_ID);
  }

  @Test
  @User(
      purchases = @Purchase(
          productId = PRODUCT_ID,
          price = 300
      )
  )
  void getAllPurchases() {
    assertThat(purchaseClient.getPurchases()).isNotEmpty();
  }

  @Test
  @User(
      purchases = @Purchase(
          productId = PRODUCT_ID,
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
          productId = PRODUCT_ID,
          price = 200
      )
  )
  void getUserPurchasesTest(UserDTO user) {
    List<PurchaseJson> userPurchases = purchaseClient.getUserPurchases(user.id());

    assertThat(userPurchases).isNotEmpty();
  }
}
