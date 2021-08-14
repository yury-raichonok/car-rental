package com.example.carrental.service;

import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostResponse;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.entity.order.Order;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

  Page<OrderInformationResponse> findAllCurrent(Pageable pageable, String language);

  Page<OrderInformationResponse> findAllFuture(Pageable pageable, String language);

  Page<OrderNewResponse> findAllNew(Pageable pageable, String language);

  Page<UserOrderResponse> findAllNewUserOrders(Pageable pageable, String language);

  Page<UserOrderResponse> findAllUserOrdersHistory(Pageable pageable, String language);

  OrderTotalCostResponse calculateTotalCost(OrderTotalCostRequest orderTotalCostRequest)
      throws OrderPeriodValidationException;

  void create(CreateOrderRequest createOrderRequest)
      throws DocumentsNotConfirmedException, OrderPeriodValidationException;

  Page<OrderResponse> findAll(OrderSearchRequest orderSearchRequest, String language);

  void approveOrder(Long id);

  void cancelOrderAfterPayment(Long id, OrderRejectRequest orderRejectRequest);

  void completeOrder(Long id);

  void completeOrderWithPenalty(Long id,
      OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest);

  void rejectOrder(Long id, OrderRejectRequest orderRejectRequest);

  void startRentalPeriod(Long id);

  void update(Long id, CreateOrderRequest createOrderRequest);

  ByteArrayResource exportOrderToPDF(Long id)
      throws FontNotFoundException, DocumentNotGeneratedException;

  Order findById(Long id);

  int findNewOrdersAmount();

  int findNewOrdersAmountPerDay();

  int findNewUserOrdersAmount(String email);

  void updatePaymentDateAndStatusToPaid(Order order);
}
