package com.example.carrental.repository;

import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * Repair Bill Criteria Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface RepairBillCriteriaRepository {

  /**
   * @param repairBillSearchRequest search parameters.
   * @return page of repair bills filtered by parameters.
   */
  Page<RepairBill> findAll(RepairBillSearchRequest repairBillSearchRequest);
}
