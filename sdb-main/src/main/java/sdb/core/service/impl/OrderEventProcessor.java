package sdb.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdb.core.model.event.OrderEvent;
import sdb.core.model.event.OrderEvent.ErrorMessage;
import sdb.core.model.order.OrderStatus;
import sdb.core.service.OrderService;
import utils.logging.Logger;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderEventProcessor {

  private final Logger logger;
  private final ObjectMapper mapper;
  private final OrderService orderService;

  public void processOrderApproval(OrderEvent event) {
    orderService.updateStatus(event.getOrderId(), OrderStatus.APPROVED);
  }

  @Transactional
  public void processOrderRejection(OrderEvent event) {
    List<ErrorMessage> errorMessages = event.getErrorMessages();
    if (errorMessages != null && !errorMessages.isEmpty()) {
      JsonNode[] reasonsArray = errorMessages.stream()
          .map(errorMessage -> {
            try {
              return mapper.valueToTree(errorMessage);
            } catch (Exception e) {
              logger.error("Ошибка при преобразовании сообщения об ошибке в JsonNode: " + e.getMessage(), e);
              return null;
            }
          })
          .filter(Objects::nonNull)
          .toArray(JsonNode[]::new);

      orderService.rejectOrder(event.getOrderId(), reasonsArray);
    } else {
      orderService.rejectOrder(event.getOrderId());
    }
    logger.info("Order with ID " + event.getOrderId() + " has been rejected");
  }
}
