package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

import com.example.carrental.controller.dto.bill.PaymentBillResponse;
import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.controller.dto.bill.UserNewPaymentBillsResponse;
import com.example.carrental.controller.dto.bill.UserPaymentBillsResponse;
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
import org.springframework.web.bind.annotation.CookieValue;
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
  public ResponseEntity<Page<PaymentBillResponse>> findAll(
      @Valid @RequestBody PaymentBillSearchRequest paymentBillSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAll(paymentBillSearchRequest, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user/new")
  public ResponseEntity<Page<UserNewPaymentBillsResponse>> findAllNewUserBills(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAllNewUserBills(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user")
  public ResponseEntity<Page<UserPaymentBillsResponse>> findAllUserBillsHistory(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAllUserBillsHistory(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @GetMapping(path = "/user/new/amount")
  public ResponseEntity<Integer> findNewUserPaymentBillsAmount() {
    var paymentBills = paymentBillService.findNewUserBillsAmount();
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> approveWithoutPayment(@NotNull @Positive @PathVariable Long id) {
    paymentBillService.approveWithoutPayment(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/pay/{id}")
  public ResponseEntity<HttpStatus> payBill(@NotNull @Positive @PathVariable Long id) {
    paymentBillService.payBill(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
