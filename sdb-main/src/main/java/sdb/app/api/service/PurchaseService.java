package sdb.app.api.service;

import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

  List<PurchaseEntity> createPurchase(PurchaseJson... purchases);

  List<PurchaseJson> getPurchases();

  Optional<PurchaseJson> getPurchase(int purchaseId);

  List<PurchaseJson> getUserPurchases(int userId);
}
