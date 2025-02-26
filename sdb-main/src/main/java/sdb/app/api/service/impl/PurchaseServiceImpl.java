package sdb.app.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;
import sdb.app.api.service.PurchaseService;

import java.util.Arrays;
import java.util.List;

@Component
public class PurchaseServiceImpl implements PurchaseService {

  @Autowired
  PurchaseDao purchaseDao;

  @Override
  public void createPurchase(PurchaseJson... purchases) {
    PurchaseEntity[] purchaseEntities = Arrays.stream(purchases)
        .map(PurchaseEntity::fromJson)
        .toArray(PurchaseEntity[]::new);

    purchaseDao.createPurchase(purchaseEntities);
  }

  @Override
  public List<PurchaseEntity> getPurchases() {
    return purchaseDao.getPurchases();
  }

  @Override
  public PurchaseEntity getPurchase(int purchaseId) {
    return purchaseDao.getPurchase(purchaseId);
  }

  @Override
  public PurchaseEntity getUserPurchases(int userId) {
    return purchaseDao.getUserPurchases(userId);
  }
}
