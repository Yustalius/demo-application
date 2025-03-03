package sdb.app.service;

import sdb.app.data.entity.purchase.PurchaseEntity;
import sdb.app.model.purchase.PurchaseJson;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

  List<PurchaseEntity> createPurchase(PurchaseJson... purchases);

  List<PurchaseJson> getPurchases();

  Optional<PurchaseJson> getPurchase(int purchaseId);

  List<PurchaseJson> getUserPurchases(int userId);
}
