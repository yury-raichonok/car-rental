package com.example.carrental.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;
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
  public ResponseEntity<Page<RepairBillResponse>> findAll(@Valid @RequestBody
      RepairBillSearchRequest repairBillSearchRequest) {
    var repairBills = repairBillService.findAll(repairBillSearchRequest);
    return new ResponseEntity<>(repairBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user")
  public ResponseEntity<?> findUserBillsHistory(Pageable pageable) {
    try {
      var paymentBills = repairBillService.findUserBillsHistory(pageable);
      return new ResponseEntity<>(paymentBills, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/user/new")
  public ResponseEntity<?> findNewUserBills(Pageable pageable) {
    try {
      var paymentBills = repairBillService.findNewUserBills(pageable);
      return new ResponseEntity<>(paymentBills, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/pay/{id}")
  public ResponseEntity<?> payBill(@NotNull @Positive @PathVariable Long id) {
    var response = repairBillService.payBill(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
