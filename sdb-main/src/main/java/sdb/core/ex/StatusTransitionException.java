package sdb.core.ex;

import static sdb.core.model.order.OrderStatusTransition.getAllowedTransitions;

import lombok.Data;
import sdb.core.model.order.OrderStatus;

@Data
public class StatusTransitionException extends RuntimeException {
  private OrderStatus currentStatus;
  private OrderStatus newStatus;

  public StatusTransitionException(String message) {
    super(message);
  }

  public StatusTransitionException(OrderStatus currentStatus, OrderStatus newStatus) {
    super(
        currentStatus == OrderStatus.FINISHED || currentStatus == OrderStatus.CANCELED ?
            "Со статуса " + currentStatus + " нельзя изменить статус заказа" :
            "Нельзя перейти со статуса " + currentStatus + " на статус " + newStatus + ". " +
                "Допустимые переходы: " + getAllowedTransitions(currentStatus)
    );
    this.currentStatus = currentStatus;
    this.newStatus = newStatus;
  }
}
