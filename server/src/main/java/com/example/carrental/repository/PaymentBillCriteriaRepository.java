package com.example.carrental.repository;

import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.entity.bill.PaymentBill;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentBillCriteriaRepository {

  Page<PaymentBill> findAll(PaymentBillSearchRequest paymentBillSearchRequest);
}
