package sdb.warehouse.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdb.warehouse.data.entity.ProductEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с товарами на складе
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    
    /**
     * Найти товар по внешнему идентификатору
     * 
     * @param externalProductId внешний идентификатор товара
     * @return товар или null, если не найден
     */
    Optional<ProductEntity> findByExternalProductId(Integer externalProductId);
    
    /**
     * Проверить наличие товара с указанным внешним идентификатором
     * 
     * @param externalProductId внешний идентификатор товара
     * @return true если товар существует, false в противном случае
     */
    boolean existsByExternalProductId(Integer externalProductId);
} 