package sdb.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.dao.impl.PurchaseDaoSpringImpl;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;
import sdb.app.api.service.PurchaseService;
import sdb.app.api.service.impl.PurchaseServiceImpl;
import sdb.app.config.Config;

import java.util.List;

import static sdb.app.api.data.Databases.dataSource;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
  private static final Config CFG = Config.getInstance();

  PurchaseDao purchaseDao = new PurchaseDaoSpringImpl(dataSource(CFG.postgresUrl()));

  @Autowired
  PurchaseService purchaseService;

  @PostMapping("/add")
  public void purchase(@RequestBody PurchaseJson... purchaseJsons) {
    System.out.println(1);
    purchaseService.createPurchase(purchaseJsons);
  }

  @GetMapping
  public List<PurchaseEntity> getPurchases() {
    List<PurchaseEntity> purchases = purchaseDao.getPurchases();
    return purchases;
  }
}
