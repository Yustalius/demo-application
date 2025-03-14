package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.purchase.PurchaseEntity;
import sdb.app.data.entity.user.UsersEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseEntity, Integer> {

  @Query("SELECT p FROM PurchaseEntity p " +
      "LEFT JOIN FETCH p.user " +
      "LEFT JOIN FETCH p.product " +
      "ORDER BY p.purchaseId ASC")
  List<PurchaseEntity> findAllWithJoins();

  List<PurchaseEntity> findByUser(UsersEntity user);
}
