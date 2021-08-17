package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

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

/**
 * The controller for Repair Bills REST endpoints.
 * <p>
 * This class handles the CRUD operations for Repair Bills, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/repairbills")
@Validated
public class RepairBillController {

  private final RepairBillService repairBillService;

  /**
   * Handle the /repairbills endpoint.
   * @param repairBillSearchRequest search parameters.
   * @param language selected language.
   * @return page of repair bills.
   */
  @PostMapping
  public ResponseEntity<Page<RepairBillResponse>> findAll(
      @Valid @RequestBody RepairBillSearchRequest repairBillSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var repairBills = repairBillService.findAll(repairBillSearchRequest, language);
    return new ResponseEntity<>(repairBills, HttpStatus.OK);
  }

  /**
   * Handle the /repairbills/user endpoint.
   * @param pageable page of repair bills.
   * @param language selected language.
   * @return page of user repair bills history.
   */
  @GetMapping(path = "/user")
  public ResponseEntity<Page<RepairBillHistoryResponse>> findAllUserBillsHistory(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = repairBillService.findAllUserBillsHistory(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /repairbills/user/new endpoint.
   * @param pageable page of repair bills.
   * @param language selected language.
   * @return page of new user repair bills.
   */
  @GetMapping(path = "/user/new")
  public ResponseEntity<Page<RepairBillNewResponse>> findAllNewUserBills(Pageable pageable,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var paymentBills = repairBillService.findAllNewUserBills(pageable, language);
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /repairbills/user/new/amount endpoint.
   * @return new user repair bills amount.
   */
  @GetMapping(path = "/user/new/amount")
  public ResponseEntity<Integer> findUserRepairBillsAmount() {
    var paymentBills = repairBillService.findNewUserRepairBillsAmount();
    return new ResponseEntity<>(paymentBills, HttpStatus.OK);
  }

  /**
   * Handle the /repairbills/pay/{id} endpoint.
   * @param id of the repair bill which paid.
   * @return status 200 if successfully paid.
   */
  @PostMapping("/pay/{id}")
  public ResponseEntity<HttpStatus> payBill(@NotNull @Positive @PathVariable Long id) {
    repairBillService.payBill(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
