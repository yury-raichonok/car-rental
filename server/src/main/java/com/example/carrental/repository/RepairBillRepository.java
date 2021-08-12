package com.example.carrental.repository;

import com.example.carrental.entity.bill.RepairBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairBillRepository extends JpaRepository<RepairBill, Long> {

  int countAllByOrder_UserEmailAndPaymentDateNull(String email);

  Page<RepairBill> findAllByOrder_UserEmailAndPaymentDateNotNull(String email, Pageable pageable);

  Page<RepairBill> findAllByOrder_UserEmailAndPaymentDateNull(String email, Pageable pageable);
}
