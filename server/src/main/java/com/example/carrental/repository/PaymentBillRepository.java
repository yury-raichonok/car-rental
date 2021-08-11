package com.example.carrental.repository;

import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.order.OrderPaymentStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, Long> {

  Page<PaymentBill> findAllByOrder_UserEmailAndPaymentDateNotNull(String email, Pageable pageable);

  Page<PaymentBill> findAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(
      LocalDateTime date, String email, OrderPaymentStatus paymentStatus, Pageable pageable);

  int countAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(LocalDateTime date,
      String email, OrderPaymentStatus paymentStatus);
}
