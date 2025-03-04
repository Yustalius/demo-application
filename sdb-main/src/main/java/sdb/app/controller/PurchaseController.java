package sdb.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdb.app.data.entity.purchase.PurchaseEntity;
import sdb.app.model.purchase.PurchaseJson;
import sdb.app.service.PurchaseService;
import sdb.app.logging.Logger;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
  private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

  @Autowired
  private Logger logger;

  @Autowired
  private PurchaseService purchaseService;

  @PostMapping("/add")
  public List<PurchaseEntity> addPurchase(@RequestBody PurchaseJson... purchaseJsons) throws JsonProcessingException {
      logger.info("Creating purchases: ", mapper.writeValueAsString(purchaseJsons));
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
