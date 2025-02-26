package sdb.app.api.service;

import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;

import java.util.List;

public interface PurchaseService {

  void createPurchase(PurchaseJson... purchases);

  List<PurchaseEntity> getPurchases();

  PurchaseEntity getPurchase(int purchaseId);

  PurchaseEntity getUserPurchases(int userId);
}
