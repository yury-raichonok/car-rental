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

@Service
public interface RepairBillService {

  Page<RepairBillResponse> findAll(RepairBillSearchRequest repairBillSearchRequest);

  String create(CreateRepairBillRequest createRepairBillRequest);

  Page<RepairBillHistoryResponse> findUserBillsHistory(Pageable pageable);

  Page<RepairBillNewResponse> findNewUserBills(Pageable pageable);

  String payBill(Long id);

  int findNewUserBillsAmount(String email);

  RepairBill findById(Long id);
}
