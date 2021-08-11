package com.example.carrental.service;

import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostResponse;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.order.Order;
import com.example.carrental.service.exceptions.CarAlreadyBookedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

  Page<OrderResponse> findAll(OrderSearchRequest orderSearchRequest);

  Page<OrderNewResponse> findAllNew(Pageable pageable);

  int findNewOrdersAmount();

  Page<OrderInformationResponse> findAllCurrent(Pageable pageable);

  Page<OrderInformationResponse> findAllFuture(Pageable pageable);

  Order findById(Long id);

  OrderTotalCostResponse calculateTotalCost(OrderTotalCostRequest orderTotalCostRequest);

  String create(CreateOrderRequest createOrderRequest)
      throws DocumentsNotConfirmedException, CarAlreadyBookedException;

  String update(Long id, CreateOrderRequest createOrderRequest);

  String rejectOrder(Long id, OrderRejectRequest orderRejectRequest);

  String approveOrder(Long id);

  String completeOrder(Long id);

  String completeOrderWithPenalty(Long id,
      OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest);

  String startRentalPeriod(Long id);

  String cancelOrderAfterPayment(Long id, OrderRejectRequest orderRejectRequest);

  int findNewOrdersAmountPerDay();

  String updatePaymentDateAndStatusToPaid(Order order);

  int findNewUserOrdersAmount(String email);

  Page<UserOrderResponse> findNewUserOrders(Pageable pageable);

  Page<UserOrderResponse> findUserOrdersHistory(Pageable pageable);

  ByteArrayResource exportOrderToPDF(Long id) throws FontNotFoundException;
}
