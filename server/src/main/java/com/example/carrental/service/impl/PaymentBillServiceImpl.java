package com.example.carrental.service.impl;

import static com.example.carrental.entity.order.OrderPaymentStatus.PAID;

import com.example.carrental.config.ApplicationPropertiesConfig;
import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.mapper.PaymentBillMapper;
import com.example.carrental.repository.PaymentBillCriteriaRepository;
import com.example.carrental.repository.PaymentBillRepository;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.UserSecurityService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for Payment bills.
 * <p>
 * This class performs the CRUD operations for Payment bills.
 * </p>
 * @author Yury Raichonak
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentBillServiceImpl implements PaymentBillService {

  private final ApplicationPropertiesConfig applicationPropertiesConfig;
  private final PaymentBillRepository paymentBillRepository;
  private final PaymentBillCriteriaRepository paymentBillCriteriaRepository;
  private final PaymentBillMapper paymentBillMapper;
  private final LocationTranslationService locationTranslationService;
  private final UserSecurityService userSecurityService;

  /**
   * @param paymentBillSearchRequest search parameters.
   * @param language selected language.
   * @return page of payment bills.
   */
  @Override
  public Page<PaymentBillResponse> findAll(PaymentBillSearchRequest paymentBillSearchRequest,
      String language) {
    var billsPage = paymentBillCriteriaRepository.findAll(paymentBillSearchRequest);
    List<PaymentBillResponse> billsResponse = new ArrayList<>();
    billsPage.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(paymentBillMapper.paymentBillToPaymentBillResponse(bill));
    });
    return new PageImpl<>(billsResponse, billsPage.getPageable(), billsPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of new user payment bills.
   */
  @Override
  public Page<UserNewPaymentBillsResponse> findAllNewUserBills(Pageable pageable, String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var paymentBills = paymentBillRepository
        .findAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(LocalDateTime.now(),
            userEmail, OrderPaymentStatus.NOT_PAID, pageable);
    List<UserNewPaymentBillsResponse> billsResponse = new ArrayList<>();
    paymentBills.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(paymentBillMapper.paymentBillToUserNewPaymentBillsResponse(bill));
    });
    return new PageImpl<>(billsResponse, paymentBills.getPageable(),
        paymentBills.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of user paid bills.
   */
  @Override
  public Page<UserPaymentBillsResponse> findAllUserBillsHistory(Pageable pageable,
      String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var paymentBills = paymentBillRepository
        .findAllByOrder_UserEmailAndPaymentDateNotNull(userEmail, pageable);
    List<UserPaymentBillsResponse> billsResponse = new ArrayList<>();
    paymentBills.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(paymentBillMapper.paymentBillToUserPaymentBillsResponse(bill));
    });
    return new PageImpl<>(billsResponse, paymentBills.getPageable(),
        paymentBills.getTotalElements());
  }

  /**
   * @param id of payment bill.
   * @throws IllegalStateException if payment bill already been paid, or it expires.
   */
  @Override
  @Transactional
  public void approveWithoutPayment(Long id) {
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
    order.setRentalStatus(OrderRentalStatus.NOT_STARTED);
    order.setPaymentDate(LocalDateTime.now());
    order.setPaymentStatus(PAID);
    paymentBillRepository.save(paymentBill);
  }

  /**
   * @param id of payment bill.
   */
  @Override
  @Transactional
  public void payBill(Long id) {
    var paymentBill = findById(id);
    var order = paymentBill.getOrder();
    paymentBill.setPaymentDate(LocalDateTime.now());
    order.setRentalStatus(OrderRentalStatus.NOT_STARTED);
    order.setPaymentDate(LocalDateTime.now());
    order.setPaymentStatus(PAID);
    paymentBillRepository.save(paymentBill);
  }

  /**
   * @param order for which creating new payment bill.
   */
  @Override
  @Transactional
  public void create(Order order) {
    paymentBillRepository.save(PaymentBill
        .builder()
        .totalCost(order.getTotalCost())
        .sentDate(LocalDateTime.now())
        .expirationTime(
            LocalDateTime.now().plusMinutes(applicationPropertiesConfig.getBillValidityPeriodInMinutes()))
        .order(order)
        .build());
  }

  /**
   * @param id of payment bill.
   * @return payment bill.
   * @throws IllegalStateException if payment bill with specified id is not exists.
   */
  @Override
  public PaymentBill findById(long id) {
    return paymentBillRepository.findById(id).orElseThrow(() -> {
      log.error("Payment bill with id {} does not exists", id);
      throw new IllegalStateException(String.format("Payment bill with id %d does not exists", id));
    });
  }

  /**
   * @return amount of new user payment bills.
   */
  @Override
  public int findNewUserBillsAmount() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    return paymentBillRepository
        .countAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(
            LocalDateTime.now(), userEmail, OrderPaymentStatus.NOT_PAID);
  }
}
