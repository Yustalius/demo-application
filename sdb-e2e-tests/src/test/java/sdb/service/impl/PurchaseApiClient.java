package sdb.service.impl;

import retrofit2.Response;
import sdb.api.PurchaseApi;
import sdb.api.core.RestClient;
import sdb.model.product.PurchaseJson;
import sdb.service.PurchaseClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseApiClient extends RestClient implements PurchaseClient {

  PurchaseApi purchaseApi;

  public PurchaseApiClient() {
    super(CFG.coreUrl());
    this.purchaseApi = create(PurchaseApi.class);
  }

  @Override
  public void createPurchase(PurchaseJson... purchases) {
    Response<Void> response;
    try {
      response = purchaseApi.addPurchases(purchases).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
  }

  @Override
  public List<PurchaseJson> getPurchases() {
    Response<List<PurchaseJson>> response;
    try {
      response = purchaseApi.getAllPurchases().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public PurchaseJson getPurchase(int purchaseId) {
    Response<PurchaseJson> response;
    try {
      response = purchaseApi.getPurchase(purchaseId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public List<PurchaseJson> getUserPurchases(int userId) {
    Response<List<PurchaseJson>> response;
    try {
      response = purchaseApi.getUserPurchases(userId).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }
}
