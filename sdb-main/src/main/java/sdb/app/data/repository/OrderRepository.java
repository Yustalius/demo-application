package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.order.OrderEntity;
import sdb.app.data.entity.user.UsersEntity;
import sdb.app.model.order.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  @Query("SELECT o FROM OrderEntity o " +
      "LEFT JOIN FETCH o.user " +
      "LEFT JOIN FETCH o.product " +
      "ORDER BY o.purchaseId ASC")
  List<OrderEntity> findAllWithJoins();

  List<OrderEntity> findByUser(UsersEntity user);
}
