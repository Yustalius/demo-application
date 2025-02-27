package sdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import sdb.model.product.PurchaseJson;

import java.util.ArrayList;
import java.util.List;

public record TestData(
    @JsonIgnore String username,
    @JsonIgnore String password,
    @JsonIgnore List<PurchaseJson> purchases
) {
  public TestData(String username, String password) {
    this(username, password, new ArrayList<>());
  }
}
