package com.example.carrental.controller;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
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

  @GetMapping(path = "/contacts")
  public ResponseEntity<RentalDetailsContactInformationResponse> getContactInformation(
      @NotNull @CookieValue(name = "i18next") String language) {
    var informationResponse = rentalDetailsService.getContactInformation(language);
    return new ResponseEntity<>(informationResponse, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<RentalDetailsResponse> getRentalDetailsResponse(
      @NotNull @CookieValue(name = "i18next") String language) {
    var informationResponse = rentalDetailsService.getRentalDetailsResponse(language);
    return new ResponseEntity<>(informationResponse, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<HttpStatus> createOrUpdate(
      @Valid @RequestBody RentalDetailsUpdateRequest rentalDetailsUpdateRequest) {
    rentalDetailsService.createOrUpdate(rentalDetailsUpdateRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
