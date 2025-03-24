package sdb.core.data.entity.order;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Сущность, представляющая причины отмены заказа.
 * Соответствует таблице cancellation_reasons в базе данных.
 */
@Entity
@Table(name = "cancellation_reasons")
@Getter
@Setter
@NoArgsConstructor
public class CancellationReasonEntity {

    /**
     * Уникальный идентификатор записи о причине отмены
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Связь с заказом, к которому относится причина отмены
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    /**
     * Причина отмены заказа в формате JSONB
     */
    @Column(name = "reason", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode reason;
} 