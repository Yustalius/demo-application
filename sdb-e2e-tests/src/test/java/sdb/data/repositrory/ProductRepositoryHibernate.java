package sdb.data.repositrory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;
import sdb.config.Config;
import sdb.data.entity.products.ProductEntity;

import java.util.Optional;

import static sdb.data.jpa.EntityManagers.em;

public class ProductRepositoryHibernate implements ProductRepository {

  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.postgresUrl());

  @NotNull
  @Override
  public ProductEntity create(ProductEntity product) {
    entityManager.joinTransaction();
    entityManager.persist(product);
    System.out.println(product.getId());
    return product;
  }

  @NotNull
  @Override
  public Optional<ProductEntity> findById(int id) {
    return Optional.ofNullable(entityManager.find(ProductEntity.class, id));
  }

  @NotNull
  @Override
  public Optional<ProductEntity> findByName(String productName) {
    try {
      return Optional.of(
          entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.productName =: productName", ProductEntity.class)
          .setParameter("productName", productName)
          .getSingleResult());
    } catch (NoResultException e) {
      System.err.println("No result");
      return Optional.empty();
    }
  }
}
