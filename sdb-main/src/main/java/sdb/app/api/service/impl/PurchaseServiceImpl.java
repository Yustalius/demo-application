package sdb.app.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;
import sdb.app.api.service.PurchaseService;
import sdb.app.logging.Logger;

import javax.sql.DataSource;
import java.util.*;

@Component
public class PurchaseServiceImpl implements PurchaseService {
  private static final Logger logger = new Logger();

  private final PurchaseDao purchaseDao;
  private final TransactionTemplate transactionTemplate;

  public PurchaseServiceImpl(
      PurchaseDao purchaseDao,
      @Qualifier("dbDatasource") DataSource dataSource) {
    this.purchaseDao = purchaseDao;
    this.transactionTemplate = new TransactionTemplate(new JdbcTransactionManager(dataSource));
  }

  @Override
  public List<PurchaseEntity> createPurchase(PurchaseJson... purchases) {
    PurchaseEntity[] purchaseEntities = Arrays.stream(purchases)
        .map(PurchaseEntity::fromJson)
        .toArray(PurchaseEntity[]::new);

    final List<PurchaseEntity> createdPurchases = new ArrayList<>();
    try {
      transactionTemplate.execute(status -> {
        for (PurchaseEntity purchaseEntity : purchaseEntities) {
          createdPurchases.add(purchaseDao.createPurchase(purchaseEntity));
        }
        return createdPurchases;
      });
      logger.info("Created purchases ", new ObjectMapper().writeValueAsString(createdPurchases));
      return createdPurchases;
    } catch (Exception e) {
      logger.error("Couldn't create purchase ", e.getMessage());
      throw new RuntimeException(e);
    }
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
        .toList();
  }
}
