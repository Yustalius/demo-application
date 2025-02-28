package sdb.app.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;
import sdb.app.api.service.PurchaseService;

import java.util.*;

@Component
public class PurchaseServiceImpl implements PurchaseService {

  @Autowired
  PurchaseDao purchaseDao;

  @Override
  public List<PurchaseEntity> createPurchase(PurchaseJson... purchases) {
    PurchaseEntity[] purchaseEntities = Arrays.stream(purchases)
        .map(PurchaseEntity::fromJson)
        .toArray(PurchaseEntity[]::new);

    final List<PurchaseEntity> createdPurchases = new ArrayList<>();
    for (PurchaseEntity purchaseEntity : purchaseEntities) {
      createdPurchases.add(purchaseDao.createPurchase(purchaseEntity));
    }

    return createdPurchases;
  }

  @Override
  public List<PurchaseJson> getPurchases() {
    Optional<List<PurchaseEntity>> purchases = purchaseDao.getPurchases();

    if (purchases.isEmpty()) {
      return Collections.emptyList();
    }

    return purchases.get().stream()
        .map(PurchaseJson::fromEntity)
        .toList();
  }

  @Override
  public Optional<PurchaseJson> getPurchase(int purchaseId) {
    Optional<PurchaseEntity> purchase = purchaseDao.getPurchase(purchaseId);

    return purchase.map(PurchaseJson::fromEntity);
  }

  @Override
  public List<PurchaseJson> getUserPurchases(int userId) {
    Optional<List<PurchaseEntity>> userPurchases = purchaseDao.getUserPurchases(userId);

    if (userPurchases.isEmpty()) {
      return Collections.emptyList();
    }

    return userPurchases.get().stream()
        .map(PurchaseJson::fromEntity)
        .toList();  }
}
