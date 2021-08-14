package com.example.carrental.repository;

import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  int countAllByRentalStatus(OrderRentalStatus rentalStatus);

  int countAllBySentDateAfter(LocalDateTime date);

  int countAllByUser_EmailAndRentalStatusAndPaymentStatus(String email, OrderRentalStatus status,
      OrderPaymentStatus paymentStatus);

  Page<Order> findAllByRentalStatus(OrderRentalStatus rentalStatus, Pageable pageable);

  Page<Order> findAllByRentalStatusAndPaymentStatus(OrderRentalStatus rentalStatus,
      OrderPaymentStatus paymentStatus, Pageable pageable);

  Page<Order> findAllByRentalStatusAndUser_EmailAndPaymentStatus(OrderRentalStatus rentalStatus,
      String email, OrderPaymentStatus paymentStatus, Pageable pageable);

  Page<Order> findAllByRentalStatusOrRentalStatusAndUser_Email(OrderRentalStatus firstStatus,
      OrderRentalStatus secondStatus, String email, Pageable pageable);
}
