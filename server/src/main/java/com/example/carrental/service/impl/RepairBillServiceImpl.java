package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.entity.bill.RepairBill;
import com.example.carrental.entity.user.User;
import com.example.carrental.mapper.RepairBillMapper;
import com.example.carrental.repository.RepairBillCriteriaRepository;
import com.example.carrental.repository.RepairBillRepository;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.RepairBillService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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

  private OrderService orderService;

  @Autowired
  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public Page<RepairBillResponse> findAll(RepairBillSearchRequest repairBillSearchRequest,
      String language) {
    var billsPage = repairBillCriteriaRepository.findAll(repairBillSearchRequest);
    List<RepairBillResponse> repairBills = new ArrayList<>();
    billsPage.forEach(bill -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      }
      repairBills.add(repairBillMapper.repairBillToRepairBillResponse(bill));
    });
    return new PageImpl<>(repairBills, billsPage.getPageable(), billsPage.getTotalElements());
  }

  @Override
  @Transactional
  public String create(CreateRepairBillRequest createRepairBillRequest) {
    var order = orderService.findById(createRepairBillRequest.getOrderId());
    repairBillRepository.save(RepairBill
        .builder()
        .totalCost(createRepairBillRequest.getTotalCost())
        .sentDate(LocalDateTime.now())
        .message(createRepairBillRequest.getMessage())
        .order(order)
        .build());
    return "Success";
  }

  @Override
  public Page<RepairBillHistoryResponse> findAllUserBillsHistory(Pageable pageable, String language) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var repairBills = repairBillRepository
        .findAllByOrder_UserEmailAndPaymentDateNotNull(user.getEmail(), pageable);
    List<RepairBillHistoryResponse> responses = new ArrayList<>();
    repairBills.forEach(bill -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      }
      responses.add(repairBillMapper.repairBillToRepairBillHistoryResponse(bill));
    });
    return new PageImpl<>(responses, repairBills.getPageable(),
        repairBills.getTotalElements());
  }

  @Override
  public Page<RepairBillNewResponse> findAllNewUserBills(Pageable pageable, String language) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }
    var repairBills = repairBillRepository
        .findAllByOrder_UserEmailAndPaymentDateNull(user.getEmail(), pageable);
    List<RepairBillNewResponse> responses = new ArrayList<>();
    repairBills.forEach(bill -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(bill.getOrder().getLocation(), language);
      }
      responses.add(repairBillMapper.repairBillToRepairBillNewResponse(bill));
    });
    return new PageImpl<>(responses, repairBills.getPageable(),
        repairBills.getTotalElements());
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
    var optionalRepairBill = repairBillRepository.findById(id);
    if (optionalRepairBill.isEmpty()) {
      log.error("Repair bill with id {} does not exists", id);
      throw new IllegalStateException(String.format("Repair bill with id %d does not exists", id));
    }
    return optionalRepairBill.get();
  }
}
