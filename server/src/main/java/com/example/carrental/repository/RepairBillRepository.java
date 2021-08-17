package com.example.carrental.repository;

import com.example.carrental.entity.bill.RepairBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repair Bill Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface RepairBillRepository extends JpaRepository<RepairBill, Long> {

  /**
   * @param email of user.
   * @return amount of new user payment bills.
   */
  int countAllByOrder_UserEmailAndPaymentDateNull(String email);

  /**
   * @param email of user.
   * @param pageable data.
   * @return page of user repair bills history.
   */
  Page<RepairBill> findAllByOrder_UserEmailAndPaymentDateNotNull(String email, Pageable pageable);

  /**
   * @param email of user.
   * @param pageable data.
   * @return page of new user repair bills.
   */
  Page<RepairBill> findAllByOrder_UserEmailAndPaymentDateNull(String email, Pageable pageable);
}
