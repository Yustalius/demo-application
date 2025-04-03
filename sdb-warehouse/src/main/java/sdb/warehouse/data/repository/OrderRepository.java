package sdb.warehouse.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.warehouse.data.entity.OrderEntity;

import java.util.List;

/**
 * Репозиторий для работы с заказами на складе
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    
    /**
     * Найти заказ по внешнему идентификатору
     * 
     * @param externalOrderId внешний идентификатор заказа
     * @return заказ или null, если не найден
     */
    List<OrderEntity> findByExternalOrderId(Integer externalOrderId);
} 