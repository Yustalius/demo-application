package sdb.app.api.data.dao;

import sdb.app.api.data.entity.product.PurchaseEntity;

import java.util.List;
import java.util.Optional;

public interface PurchaseDao {

  PurchaseEntity createPurchase(PurchaseEntity purchase);

  Optional<List<PurchaseEntity>> getPurchases();

  Optional<PurchaseEntity> getPurchase(int purchaseId);

  Optional<List<PurchaseEntity>> getUserPurchases(int userId);
}
