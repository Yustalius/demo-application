package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.order.OrderEntity3;
import sdb.app.data.entity.user.UsersEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity3, Integer> {

  List<OrderEntity3> findByUser(UsersEntity users);
}
