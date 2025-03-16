package sdb.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.app.data.entity.order.OrderEntity3;

@Repository
public interface Order3Repository extends JpaRepository<OrderEntity3, Integer> {
}
