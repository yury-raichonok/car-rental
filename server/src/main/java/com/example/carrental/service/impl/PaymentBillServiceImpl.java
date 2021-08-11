package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.bill.CreatePaymentBillRequest;
import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.entity.user.User;
import com.example.carrental.mapper.PaymentBillMapper;
import com.example.carrental.repository.PaymentBillCriteriaRepository;
import com.example.carrental.repository.PaymentBillRepository;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.RentalDetailsService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentBillServiceImpl implements PaymentBillService {

  private final PaymentBillRepository paymentBillRepository;
  private final PaymentBillCriteriaRepository paymentBillCriteriaRepository;
  private final PaymentBillMapper paymentBillMapper;

  private OrderService orderService;
  private RentalDetailsService rentalDetailsService;

  @Autowired
  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @Autowired
  public void setRentalDetailsService(RentalDetailsService rentalDetailsService) {
    this.rentalDetailsService = rentalDetailsService;
  }

  @Override
  public Page<PaymentBillResponse> findAll(PaymentBillSearchRequest paymentBillSearchRequest) {
    var billsPage = paymentBillCriteriaRepository.findAll(paymentBillSearchRequest);
    List<PaymentBillResponse> paymentBills = new ArrayList<>();
    billsPage.forEach(
        bill -> paymentBills.add(paymentBillMapper.paymentBillToPaymentBillResponse(bill)));
    return new PageImpl<>(paymentBills, billsPage.getPageable(), billsPage.getTotalElements());
  }

  @Override
  public Page<UserPaymentBillsResponse> findUserBillsHistory(Pageable pageable) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var paymentBills = paymentBillRepository
        .findAllByOrder_UserEmailAndPaymentDateNotNull(user.getEmail(), pageable);
    List<UserPaymentBillsResponse> responses = new ArrayList<>();
    paymentBills.forEach(
        bill -> responses.add(paymentBillMapper.paymentBillToUserPaymentBillsResponse(bill)));
    return new PageImpl<>(responses, paymentBills.getPageable(), paymentBills.getTotalElements());
  }

  @Override
  public Page<UserNewPaymentBillsResponse> findNewUserBills(Pageable pageable) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var paymentBills = paymentBillRepository
        .findAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(LocalDateTime.now(),
            user.getEmail(), OrderPaymentStatus.NOT_PAID, pageable);
    List<UserNewPaymentBillsResponse> responses = new ArrayList<>();
    paymentBills.forEach(
        bill -> responses.add(paymentBillMapper.paymentBillToUserNewPaymentBillsResponse(bill)));
    return new PageImpl<>(responses, paymentBills.getPageable(), paymentBills.getTotalElements());
  }

  @Override
  public PaymentBill findById(long id) {
    var optionalPaymentBill = paymentBillRepository.findById(id);
    if (optionalPaymentBill.isEmpty()) {
      log.error("Payment bill with id {} does not exists", id);
      throw new IllegalStateException(String.format("Payment bill with id %d does not exists", id));
    }
    return optionalPaymentBill.get();
  }

  @Override
  @Transactional
  public String create(CreatePaymentBillRequest createPaymentBillRequest) {
    var order = orderService.findById(createPaymentBillRequest.getOrderId());
    var orderDetails = rentalDetailsService.getRentalDetails();

    paymentBillRepository.save(PaymentBill
        .builder()
        .totalCost(order.getTotalCost())
        .sentDate(LocalDateTime.now())
        .expirationTime(
            LocalDateTime.now().plusMinutes(orderDetails.getPaymentBillValidityPeriodInMinutes()))
        .order(order)
        .build());
    return "Success";
  }

  @Override
  @Transactional
  public String approveWithoutPayment(Long id) {
    var paymentBill = findById(id);
    var order = paymentBill.getOrder();

    if (Optional.ofNullable(paymentBill.getPaymentDate()).isPresent()) {
      log.error("Bill with id {} has already been paid!", id);
      throw new IllegalStateException(String.format("Bill with id %s has already been paid", id));
    }
    if (paymentBill.getExpirationTime().isBefore(LocalDateTime.now())) {
      log.error("Bill with id {} already expires!", id);
      throw new IllegalStateException(String.format("Bill with id %s already expires", id));
    }
    paymentBill.setPaymentDate(LocalDateTime.now());
    paymentBillRepository.save(paymentBill);

    orderService.updatePaymentDateAndStatusToPaid(order);
    return "Success";
  }

  @Override
  @Transactional
  public String payBill(Long id) {
    var paymentBill = findById(id);
    var order = paymentBill.getOrder();
    paymentBill.setPaymentDate(LocalDateTime.now());
    paymentBillRepository.save(paymentBill);

    order.setRentalStatus(OrderRentalStatus.NOT_STARTED);
    orderService.updatePaymentDateAndStatusToPaid(order);
    return "Success";
  }

  @Override
  public int findNewUserBillsAmount(String email) {
    return paymentBillRepository
        .countAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(
            LocalDateTime.now(), email, OrderPaymentStatus.NOT_PAID);
  }
}
