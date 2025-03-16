package sdb.app.ex;

import static sdb.app.model.order.OrderStatusTransition.getAllowedTransitions;

import sdb.app.model.order.OrderStatus;

public class StatusTransitionException extends RuntimeException {
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
  }
}
