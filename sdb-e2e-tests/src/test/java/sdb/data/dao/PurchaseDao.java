package sdb.data.dao;


import sdb.data.entity.purchases.PurchaseEntity;

import java.util.List;
import java.util.Optional;

public interface PurchaseDao {

  void createPurchase(PurchaseEntity... purchases);

  Optional<List<PurchaseEntity>> getPurchases();

  Optional<PurchaseEntity> getPurchase(int purchaseId);

  Optional<List<PurchaseEntity>> getUserPurchases(int userId);
}
