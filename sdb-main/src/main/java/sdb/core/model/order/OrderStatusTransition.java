package sdb.core.model.order;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static sdb.core.model.order.OrderStatus.*;
import static sdb.core.model.order.OrderStatus.REJECTED;

/**
 * Класс для описания статусной модели заказа.
 * Определяет допустимые переходы между статусами.
 */
public class OrderStatusTransition {
  
  // Карта допустимых переходов между статусами
  private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS;
  
  static {
    Map<OrderStatus, Set<OrderStatus>> transitions = new EnumMap<>(OrderStatus.class);
    
    // С PENDING можно перейти на APPROVED, REJECTED, CANCELED
    transitions.put(PENDING, Set.of(
        APPROVED,
        REJECTED,
        CANCELLED
    ));
    
    // С APPROVED можно перейти на IN_WORK, CANCELED
    transitions.put(APPROVED, Set.of(
        IN_WORK,
        CANCELLED
    ));
    
    // С REJECTED можно перейти на IN_WORK, CANCELED
    transitions.put(REJECTED, Set.of(
        IN_WORK,
        CANCELLED
    ));
    
    // С IN_WORK можно перейти на FINISHED, CANCELED
    transitions.put(IN_WORK, Set.of(
        FINISHED,
        CANCELLED
    ));
    
    // С FINISHED и CANCELED никуда нельзя перейти
    transitions.put(FINISHED, Collections.emptySet());
    transitions.put(CANCELLED, Collections.emptySet());
    
    ALLOWED_TRANSITIONS = Collections.unmodifiableMap(transitions);
  }
  
  /**
   * Проверяет, допустим ли переход из текущего статуса в новый.
   *
   * @param currentStatus текущий статус заказа
   * @param newStatus новый статус заказа
   * @return true, если переход допустим, иначе false
   */
  public static boolean isTransitionAllowed(OrderStatus currentStatus, OrderStatus newStatus) {
    // Проверяем, есть ли новый статус в списке допустимых переходов
    Set<OrderStatus> allowedStatuses = ALLOWED_TRANSITIONS.get(currentStatus);
    return allowedStatuses != null && allowedStatuses.contains(newStatus);
  }
  
  /**
   * Возвращает множество допустимых статусов, в которые можно перейти из текущего.
   *
   * @param currentStatus текущий статус заказа
   * @return множество допустимых статусов
   */
  public static Set<OrderStatus> getAllowedTransitions(OrderStatus currentStatus) {
    return ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Collections.emptySet());
  }
} 