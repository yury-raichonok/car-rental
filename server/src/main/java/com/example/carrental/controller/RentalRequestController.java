package com.example.carrental.controller;

import com.example.carrental.controller.dto.rentalDetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestResponse;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
@Validated
public class RentalRequestController {

  private final RentalRequestService rentalRequestService;

  @GetMapping
  public ResponseEntity<Page<RentalAllRequestResponse>> findAll(Pageable pageable) {
    var rentalRequests = rentalRequestService.findAll(pageable);
    return new ResponseEntity<>(rentalRequests, HttpStatus.OK);
  }

  @GetMapping(path = "/new")
  public ResponseEntity<Page<RentalRequestResponse>> findAllNew(Pageable pageable) {
    var rentalRequests = rentalRequestService.findAllNew(pageable);
    return new ResponseEntity<>(rentalRequests, HttpStatus.OK);
  }

  @GetMapping(path = "/drivinglicenses/{id}")
  public ResponseEntity<UserDrivingLicenseConfirmationDataResponse> findRequestDrivingLicenseData(
      @NotNull @Positive @PathVariable Long id) {
    var response = rentalRequestService.findRequestDrivingLicenseData(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(path = "/passports/{id}")
  public ResponseEntity<UserPassportConfirmationDataResponse> findRequestPassportData(
      @NotNull @Positive @PathVariable Long id) {
    var response = rentalRequestService.findRequestPassportData(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateRentalRequestRequest createRentalRequestRequest) {
    rentalRequestService.create(createRentalRequestRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/approve/{id}")
  public ResponseEntity<HttpStatus> approveRequest(@NotNull @Positive @PathVariable Long id) {
    rentalRequestService.approveRequest(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/reject/{id}")
  public ResponseEntity<HttpStatus> rejectRequest(@NotNull @Positive @PathVariable Long id,
      @RequestBody RentalRequestRejectRequest rentalRequestRejectRequest) {
    rentalRequestService.rejectRequest(id, rentalRequestRejectRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
