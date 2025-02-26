package sdb.app.api.data.dao;

import sdb.app.api.data.entity.product.PurchaseEntity;

import java.util.List;

public interface PurchaseDao {

  void createPurchase(PurchaseEntity... purchases);

  List<PurchaseEntity> getPurchases();

  PurchaseEntity getPurchase(int purchaseId);

  PurchaseEntity getUserPurchases(int userId);
}
