package com.example.carrental.controller;

import com.example.carrental.controller.dto.rentalDetails.RentalDetailsUpdateRequest;
import com.example.carrental.service.RentalDetailsService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/details")
@Validated
public class RentalDetailsController {

  private final RentalDetailsService rentalDetailsService;

  @GetMapping
  public ResponseEntity<?> getRentalDetailsAndStatistic(
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var informationResponse = rentalDetailsService.getRentalDetailsAndStatistic(language);
      return new ResponseEntity<>(informationResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/contacts")
  public ResponseEntity<?> getContactInformation(
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var informationResponse = rentalDetailsService.getContactInformation(language);
      return new ResponseEntity<>(informationResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping
  public ResponseEntity<String> createOrUpdate(
      @Valid @RequestBody RentalDetailsUpdateRequest rentalDetailsUpdateRequest) {
    try {
      var response = rentalDetailsService.createOrUpdate(rentalDetailsUpdateRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/admin")
  public ResponseEntity<?> getAdminDetailsStatistic() {
    try {
      var informationResponse = rentalDetailsService.getAdminDetailsStatistic();
      return new ResponseEntity<>(informationResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/user")
  public ResponseEntity<?> getUserDetailsStatistic() {
    try {
      var informationResponse = rentalDetailsService.getUserDetailsStatistic();
      return new ResponseEntity<>(informationResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
