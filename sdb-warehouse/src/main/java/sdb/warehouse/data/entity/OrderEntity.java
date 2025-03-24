package sdb.warehouse.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Сущность заказа в системе склада.
 * Соответствует таблице orders в базе данных.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    
    /**
     * Уникальный идентификатор заказа в системе склада
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Внешний идентификатор заказа (из основной системы)
     */
    @Column(name = "external_order_id", nullable = false)
    private Integer externalOrderId;
    
    /**
     * Связь с товаром
     */
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;
    
    /**
     * Количество единиц товара
     */
    @Column(nullable = false)
    private Integer quantity;
    
    /**
     * Статус заказа на складе
     */
    @Column(nullable = false)
    private String status;
}
