package com.example.carrental.service;

import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The service for Repair Bills.
 * <p>
 * This interface describes actions on Repair Bills.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface RepairBillService {

  Page<RepairBillResponse> findAll(RepairBillSearchRequest repairBillSearchRequest, String language);

  void create(CreateRepairBillRequest createRepairBillRequest);

  Page<RepairBillHistoryResponse> findAllUserBillsHistory(Pageable pageable, String language);

  Page<RepairBillNewResponse> findAllNewUserBills(Pageable pageable, String language);

  void payBill(Long id);

  int findNewUserRepairBillsAmount();

  RepairBill findById(Long id);
}
