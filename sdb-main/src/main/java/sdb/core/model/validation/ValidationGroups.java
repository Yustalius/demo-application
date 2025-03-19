package sdb.core.model.validation;

/**
 * Группы валидации для различных операций
 */
public interface ValidationGroups {
  
  /**
   * Группа валидации для создания заказа
   */
  interface Create {}
  
  /**
   * Группа валидации для обновления статуса заказа
   */
  interface UpdateStatus {}
} 