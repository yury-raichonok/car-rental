package com.example.carrental.repository;

import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairBillCriteriaRepository {

  Page<RepairBill> findAll(RepairBillSearchRequest repairBillSearchRequest);
}
