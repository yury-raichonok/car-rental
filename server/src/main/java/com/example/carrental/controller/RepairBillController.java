package com.example.carrental.controller;

import com.example.carrental.controller.dto.bill.RepairBillHistoryResponse;
import com.example.carrental.controller.dto.bill.RepairBillNewResponse;
import com.example.carrental.controller.dto.bill.RepairBillResponse;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
import com.example.carrental.service.RepairBillService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/repairbills")
@Validated
public class RepairBillController {

  private final RepairBillService repairBillService;

  @PostMapping
  public ResponseEntity<Page<RepairBillResponse>> findAll(
      @Valid @RequestBody RepairBillSearchRequest repairBillSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    var repairBills = repairBillService.findAll(repairBillSearchRequest, language);
    return new ResponseEntity<>(repairBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user")
  public ResponseEntity<Page<RepairBillHistoryResponse>> findAllUserBillsHistory(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var paymentBills = repairBillService.findAllUserBillsHistory(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user/new")
  public ResponseEntity<Page<RepairBillNewResponse>> findAllNewUserBills(Pageable pageable,
      @NotNull @CookieValue(name = "i18next") String language) {
    var paymentBills = repairBillService.findAllNewUserBills(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @PostMapping("/pay/{id}")
  public ResponseEntity<HttpStatus> payBill(@NotNull @Positive @PathVariable Long id) {
    repairBillService.payBill(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
