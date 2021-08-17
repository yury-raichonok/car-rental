package com.example.carrental.repository;

import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Order Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * @param rentalStatus of order.
   * @return amount of orders.
   */
  int countAllByRentalStatus(OrderRentalStatus rentalStatus);

  /**
   * @param date data.
   * @return amount of orders after date.
   */
  int countAllBySentDateAfter(LocalDateTime date);

  /**
   * @param email of user.
   * @param status of order.
   * @param paymentStatus of order.
   * @return amount of user orders by rental status and payment status.
   */
  int countAllByUser_EmailAndRentalStatusAndPaymentStatus(String email, OrderRentalStatus status,
      OrderPaymentStatus paymentStatus);

  /**
   * @param rentalStatus of order.
   * @param pageable data.
   * @return page of orders.
   */
  Page<Order> findAllByRentalStatus(OrderRentalStatus rentalStatus, Pageable pageable);

  /**
   * @param rentalStatus of order.
   * @param paymentStatus of order.
   * @param pageable data.
   * @return page of orders.
   */
  Page<Order> findAllByRentalStatusAndPaymentStatus(OrderRentalStatus rentalStatus,
      OrderPaymentStatus paymentStatus, Pageable pageable);

  /**
   * @param rentalStatus of order.
   * @param email of user.
   * @param paymentStatus of order.
   * @param pageable data.
   * @return page of user orders.
   */
  Page<Order> findAllByRentalStatusAndUser_EmailAndPaymentStatus(OrderRentalStatus rentalStatus,
      String email, OrderPaymentStatus paymentStatus, Pageable pageable);

  /**
   * @param firstStatus of order.
   * @param secondStatus of order.
   * @param email of user.
   * @param pageable data.
   * @return page user of orders by one of statuses.
   */
  Page<Order> findAllByRentalStatusOrRentalStatusAndUser_Email(OrderRentalStatus firstStatus,
      OrderRentalStatus secondStatus, String email, Pageable pageable);
}
