package sdb.core.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.core.data.entity.order.CancellationReasonEntity;
import sdb.core.data.entity.order.OrderEntity;

import java.util.List;

/**
 * Репозиторий для работы с причинами отмены заказов
 */
@Repository
public interface CancellationReasonRepository extends JpaRepository<CancellationReasonEntity, Integer> {
    
    /**
     * Найти все причины отмены по заказу
     * 
     * @param order заказ
     * @return список причин отмены
     */
    List<CancellationReasonEntity> findByOrder(OrderEntity order);
    
    /**
     * Найти все причины отмены по ID заказа
     * 
     * @param orderId ID заказа
     * @return список причин отмены
     */
    List<CancellationReasonEntity> findByOrderOrderId(Integer orderId);
} 