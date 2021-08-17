package com.example.carrental.repository;

import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.entity.bill.PaymentBill;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * Payment Bill Criteria Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface PaymentBillCriteriaRepository {

  /**
   * @param paymentBillSearchRequest data.
   * @return page of payment bills.
   */
  Page<PaymentBill> findAll(PaymentBillSearchRequest paymentBillSearchRequest);
}
