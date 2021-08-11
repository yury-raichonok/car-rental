package com.example.carrental.controller;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.service.PaymentBillService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paymentbills")
@Validated
public class PaymentBillController {

  private final PaymentBillService paymentBillService;

  @PostMapping
  public ResponseEntity<Page<PaymentBillResponse>> findAll(@Valid @RequestBody
      PaymentBillSearchRequest paymentBillSearchRequest) {
    var paymentBills = paymentBillService.findAll(paymentBillSearchRequest);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user")
  public ResponseEntity<?> findUserBillsHistory(Pageable pageable) {
    try {
      var paymentBills = paymentBillService.findUserBillsHistory(pageable);
      return new ResponseEntity<>(paymentBills, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/user/new")
  public ResponseEntity<?> findNewUserBills(Pageable pageable) {
    try {
      var paymentBills = paymentBillService.findNewUserBills(pageable);
      return new ResponseEntity<>(paymentBills, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> approveWithoutPayment(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = paymentBillService.approveWithoutPayment(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/pay/{id}")
  public ResponseEntity<?> payBill(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = paymentBillService.payBill(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
