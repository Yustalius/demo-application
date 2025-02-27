package sdb.service;


import sdb.model.product.PurchaseJson;
import sdb.service.impl.PurchaseApiClient;

import java.util.List;

public interface PurchaseClient {

  static PurchaseClient getInstance() {
    return new PurchaseApiClient();
  }

  void createPurchase(PurchaseJson... purchases);

  List<PurchaseJson> getPurchases();

  PurchaseJson getPurchase(int purchaseId);

  List<PurchaseJson> getUserPurchases(int userId);
}
