package sdb.app.data.dao;

import sdb.app.data.entity.purchase.PurchaseEntityOld;

import java.util.List;
import java.util.Optional;

public interface PurchaseDao {

  PurchaseEntityOld createPurchase(PurchaseEntityOld purchase);

  Optional<List<PurchaseEntityOld>> getPurchases();

  Optional<PurchaseEntityOld> getPurchase(int purchaseId);

  Optional<List<PurchaseEntityOld>> getUserPurchases(int userId);
}
