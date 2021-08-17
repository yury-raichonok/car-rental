package com.example.carrental.repository;

import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.order.OrderPaymentStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Payment Bill Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, Long> {

  /**
   * @param date data.
   * @param email of user.
   * @param paymentStatus of order.
   * @return amount of user active payment bills.
   */
  int countAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(LocalDateTime date,
      String email, OrderPaymentStatus paymentStatus);

  /**
   * @param date data.
   * @param email of user.
   * @param paymentStatus of order.
   * @param pageable data.
   * @return page of user active payment bills.
   */
  Page<PaymentBill> findAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(
      LocalDateTime date, String email, OrderPaymentStatus paymentStatus, Pageable pageable);

  /**
   * @param email of user.
   * @param pageable data.
   * @return page of user payment bills after payment.
   */
  Page<PaymentBill> findAllByOrder_UserEmailAndPaymentDateNotNull(String email, Pageable pageable);
}
