package com.example.carrental.controller;

import com.example.carrental.controller.dto.rentaldetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.service.RentalRequestService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for Rental Requests REST endpoints.
 * <p>
 * This class handles the CRUD operations for Rental Requests, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
@Validated
public class RentalRequestController {

  private final RentalRequestService rentalRequestService;

  /**
   * Handle the /requests endpoint.
   * @param pageable page of requests.
   * @return page of all rental requests.
   */
  @GetMapping
  public ResponseEntity<Page<RentalAllRequestResponse>> findAll(Pageable pageable) {
    var rentalRequests = rentalRequestService.findAll(pageable);
    return new ResponseEntity<>(rentalRequests, HttpStatus.OK);
  }

  /**
   * Handle the /requests/new endpoint.
   * @param pageable page of requests.
   * @return page of all new rental requests.
   */
  @GetMapping(path = "/new")
  public ResponseEntity<Page<RentalRequestResponse>> findAllNew(Pageable pageable) {
    var rentalRequests = rentalRequestService.findAllNew(pageable);
    return new ResponseEntity<>(rentalRequests, HttpStatus.OK);
  }

  /**
   * Handle the /requests/drivinglicenses/{id} endpoint.
   * @param id of the requested driving license.
   * @return user driving license data for confirmation request.
   */
  @GetMapping(path = "/drivinglicenses/{id}")
  public ResponseEntity<UserDrivingLicenseConfirmationDataResponse> findRequestDrivingLicenseData(
      @NotNull @Positive @PathVariable Long id) {
    var response = rentalRequestService.findRequestDrivingLicenseData(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /requests/passports/{id} endpoint.
   * @param id of the requested passport.
   * @return user passport data for confirmation request.
   */
  @GetMapping(path = "/passports/{id}")
  public ResponseEntity<UserPassportConfirmationDataResponse> findRequestPassportData(
      @NotNull @Positive @PathVariable Long id) {
    var response = rentalRequestService.findRequestPassportData(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /requests/new/amount endpoint.
   * @return new requests amount.
   */
  @GetMapping(path = "/new/amount")
  public ResponseEntity<Integer> findNewRequestsAmount() {
    var response = rentalRequestService.findNewRequestsAmount();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /requests/new/amount/day endpoint.
   * @return new requests amount per day.
   */
  @GetMapping(path = "/new/amount/day")
  public ResponseEntity<Integer> findNewRequestsAmountPerDay() {
    var response = rentalRequestService.findNewRequestsAmountPerDay();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /requests endpoint.
   * @param createRentalRequestRequest request with parameters.
   * @return status 200 if request successfully created.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateRentalRequestRequest createRentalRequestRequest) {
    rentalRequestService.create(createRentalRequestRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /requests/approve/{id} endpoint.
   * @param id of the request which approved.
   * @return status 200 if request successfully approved.
   */
  @PutMapping(path = "/approve/{id}")
  public ResponseEntity<HttpStatus> approveRequest(@NotNull @Positive @PathVariable Long id) {
    rentalRequestService.approveRequest(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /requests/reject/{id} endpoint.
   * @param id of the request which rejected.
   * @param rentalRequestRejectRequest request with parameters.
   * @return status 200 if request successfully rejected.
   */
  @PutMapping(path = "/reject/{id}")
  public ResponseEntity<HttpStatus> rejectRequest(@NotNull @Positive @PathVariable Long id,
      @RequestBody RentalRequestRejectRequest rentalRequestRejectRequest) {
    rentalRequestService.rejectRequest(id, rentalRequestRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
