package com.example.carrental.service;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The service for Payment Bills.
 * <p>
 * This interface describes actions on Payment Bills.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface PaymentBillService {

  Page<PaymentBillResponse> findAll(PaymentBillSearchRequest paymentBillSearchRequest,
      String language);

  Page<UserNewPaymentBillsResponse> findAllNewUserBills(Pageable pageable, String language);

  Page<UserPaymentBillsResponse> findAllUserBillsHistory(Pageable pageable, String language);

  void approveWithoutPayment(Long id);

  void payBill(Long id);

  void create(Order order);

  PaymentBill findById(long id);

  int findNewUserBillsAmount();
}
