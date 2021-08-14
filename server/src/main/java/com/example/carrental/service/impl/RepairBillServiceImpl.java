package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import com.example.carrental.mapper.RepairBillMapper;
import com.example.carrental.repository.RepairBillCriteriaRepository;
import com.example.carrental.repository.RepairBillRepository;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.RepairBillService;
import com.example.carrental.service.UserSecurityService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepairBillServiceImpl implements RepairBillService {

  private final RepairBillRepository repairBillRepository;
  private final RepairBillCriteriaRepository repairBillCriteriaRepository;
  private final RepairBillMapper repairBillMapper;
  private final LocationTranslationService locationTranslationService;
  private final UserSecurityService userSecurityService;

  private OrderService orderService;

  @Autowired
  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public Page<RepairBillResponse> findAll(RepairBillSearchRequest repairBillSearchRequest,
      String language) {
    var billsPage = repairBillCriteriaRepository.findAll(repairBillSearchRequest);
    List<RepairBillResponse> billsResponse = new ArrayList<>();
    billsPage.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(repairBillMapper.repairBillToRepairBillResponse(bill));
    });
    return new PageImpl<>(billsResponse, billsPage.getPageable(), billsPage.getTotalElements());
  }

  @Override
  @Transactional
  public void create(CreateRepairBillRequest createRepairBillRequest) {
    var order = orderService.findById(createRepairBillRequest.getOrderId());
    repairBillRepository.save(RepairBill
        .builder()
        .totalCost(createRepairBillRequest.getTotalCost())
        .sentDate(LocalDateTime.now())
        .message(createRepairBillRequest.getMessage())
        .order(order)
        .build());
  }

  @Override
  public Page<RepairBillHistoryResponse> findAllUserBillsHistory(Pageable pageable,
      String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var repairBills = repairBillRepository.findAllByOrder_UserEmailAndPaymentDateNotNull(userEmail,
        pageable);
    List<RepairBillHistoryResponse> billsResponse = new ArrayList<>();
    repairBills.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(repairBillMapper.repairBillToRepairBillHistoryResponse(bill));
    });
    return new PageImpl<>(billsResponse, repairBills.getPageable(), repairBills.getTotalElements());
  }

  @Override
  public Page<RepairBillNewResponse> findAllNewUserBills(Pageable pageable, String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var repairBills = repairBillRepository.findAllByOrder_UserEmailAndPaymentDateNull(userEmail,
        pageable);
    List<RepairBillNewResponse> billsResponse = new ArrayList<>();
    repairBills.forEach(bill -> {
      locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      billsResponse.add(repairBillMapper.repairBillToRepairBillNewResponse(bill));
    });
    return new PageImpl<>(billsResponse, repairBills.getPageable(), repairBills.getTotalElements());
  }

  @Override
  public void payBill(Long id) {
    var repairBill = findById(id);
    var order = repairBill.getOrder();
    repairBill.setPaymentDate(LocalDateTime.now());
    repairBillRepository.save(repairBill);
    orderService.updatePaymentDateAndStatusToPaid(order);
  }

  @Override
  public int findNewUserBillsAmount(String email) {
    return repairBillRepository.countAllByOrder_UserEmailAndPaymentDateNull(email);
  }

  @Override
  public RepairBill findById(Long id) {
    return repairBillRepository.findById(id).orElseThrow(() -> {
      log.error("Repair bill with id {} does not exists", id);
      throw new IllegalStateException(String.format("Repair bill with id %d does not exists", id));
    });
  }
}
