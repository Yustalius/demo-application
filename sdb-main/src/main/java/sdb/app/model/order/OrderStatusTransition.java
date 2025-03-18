package sdb.app.model.order;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

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
    transitions.put(OrderStatus.PENDING, Set.of(
        OrderStatus.APPROVED,
        OrderStatus.REJECTED,
        OrderStatus.CANCELED
    ));
    
    // С APPROVED можно перейти на IN_WORK, CANCELED
    transitions.put(OrderStatus.APPROVED, Set.of(
        OrderStatus.IN_WORK,
        OrderStatus.CANCELED
    ));
    
    // С REJECTED можно перейти на IN_WORK, CANCELED
    transitions.put(OrderStatus.REJECTED, Set.of(
        OrderStatus.IN_WORK,
        OrderStatus.CANCELED
    ));
    
    // С IN_WORK можно перейти на FINISHED, CANCELED
    transitions.put(OrderStatus.IN_WORK, Set.of(
        OrderStatus.FINISHED,
        OrderStatus.CANCELED
    ));
    
    // С FINISHED и CANCELED никуда нельзя перейти
    transitions.put(OrderStatus.FINISHED, Collections.emptySet());
    transitions.put(OrderStatus.CANCELED, Collections.emptySet());
    
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