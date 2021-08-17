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

/**
 * The controller for Payment Bill REST endpoints.
 * <p>
 * This class handles the CRUD operations for Payment Bill, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/paymentbills")
@Validated
public class PaymentBillController {

  private final PaymentBillService paymentBillService;

  /**
   * Handle the /paymentbills endpoint.
   * @param paymentBillSearchRequest search parameters.
   * @param language selected language.
   * @return page of all payment bills.
   */
  @PostMapping
  public ResponseEntity<Page<PaymentBillResponse>> findAll(
      @Valid @RequestBody PaymentBillSearchRequest paymentBillSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAll(paymentBillSearchRequest, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /paymentbills/user/new endpoint.
   * @param pageable page of payment bills.
   * @param language selected language.
   * @return page of all user payment bills.
   */
  @GetMapping(path = "/user/new")
  public ResponseEntity<Page<UserNewPaymentBillsResponse>> findAllNewUserBills(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAllNewUserBills(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /paymentbills/user endpoint.
   * @param pageable page of payment bills.
   * @param language selected language.
   * @return page of user payment bills history.
   */
  @GetMapping(path = "/user")
  public ResponseEntity<Page<UserPaymentBillsResponse>> findAllUserBillsHistory(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = paymentBillService.findAllUserBillsHistory(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /paymentbills/user/new/amount endpoint.
   * @return new user payment bills amount.
   */
  @GetMapping(path = "/user/new/amount")
  public ResponseEntity<Integer> findNewUserPaymentBillsAmount() {
    var paymentBills = paymentBillService.findNewUserBillsAmount();
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /paymentbills/{id} endpoint.
   * @param id of the payment bill which approved.
   * @return status 200 if payment bill successfully approved.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> approveWithoutPayment(@NotNull @Positive @PathVariable Long id) {
    paymentBillService.approveWithoutPayment(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /paymentbills/pay/{id} endpoint.
   * @param id of the payment bill which paid.
   * @return status 200 if payment bill successfully paid.
   */
  @PostMapping("/pay/{id}")
  public ResponseEntity<HttpStatus> payBill(@NotNull @Positive @PathVariable Long id) {
    paymentBillService.payBill(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
