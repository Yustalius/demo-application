package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.order.OrderEntity;
import sdb.app.data.entity.user.UserCredsEntity;
import sdb.app.data.entity.user.UsersEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  List<OrderEntity> findByUser(UsersEntity users);
}
