package sdb.app.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdb.app.api.data.dao.PurchaseDao;
import sdb.app.api.data.dao.impl.PurchaseDaoSpringImpl;
import sdb.app.api.data.entity.product.PurchaseEntity;
import sdb.app.api.model.product.PurchaseJson;
import sdb.app.api.service.PurchaseService;
import sdb.app.api.service.impl.PurchaseServiceImpl;
import sdb.app.config.Config;
import sdb.app.logging.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static sdb.app.api.data.Databases.dataSource;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
  private static final Logger logger = new Logger();
  private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

  @Autowired
  private PurchaseService purchaseService;

  @PostMapping("/add")
  public List<PurchaseEntity> purchase(@RequestBody PurchaseJson... purchaseJsons) throws JsonProcessingException {
      logger.info("Creating purchases: {}", mapper.writeValueAsString(purchaseJsons));
      return purchaseService.createPurchase(purchaseJsons);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<PurchaseJson>> getUserPurchases(
      @PathVariable int id
  ) {
    return ResponseEntity.ok(purchaseService.getUserPurchases(id));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PurchaseJson> getPurchase(
      @PathVariable int id
  ) {
    logger.info("Get purchase id = ", id);
    Optional<PurchaseJson> purchase = purchaseService.getPurchase(id);

    if (purchase.isEmpty()) {
      logger.info("Not found purchase id = ", id);
      return ResponseEntity.notFound().build();
    } else return ResponseEntity.ok(purchase.get());
  }

  @GetMapping
  public List<PurchaseJson> getPurchases() {
    return purchaseService.getPurchases();
  }
}
