package com.example.carrental.service;

import com.example.carrental.controller.dto.bill.CreatePaymentBillRequest;
import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
import com.example.carrental.entity.bill.PaymentBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PaymentBillService {

  Page<PaymentBillResponse> findAll(PaymentBillSearchRequest paymentBillSearchRequest);

  PaymentBill findById(long id);

  String create(CreatePaymentBillRequest createPaymentBillRequest);

  String approveWithoutPayment(Long id);

  Page<UserPaymentBillsResponse> findUserBillsHistory(Pageable pageable);

  Page<UserNewPaymentBillsResponse> findNewUserBills(Pageable pageable);

  String payBill(Long id);

  int findNewUserBillsAmount(String email);
}
