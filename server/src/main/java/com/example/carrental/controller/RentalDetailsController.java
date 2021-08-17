package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

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

/**
 * The controller for Rental Details REST endpoints.
 * <p>
 * This class handles the CRUD operations for Rental Details, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/details")
@Validated
public class RentalDetailsController {

  private final RentalDetailsService rentalDetailsService;

  /**
   * Handle the /details/contacts endpoint.
   * @param language selected language.
   * @return rental contact information.
   */
  @GetMapping(path = "/contacts")
  public ResponseEntity<RentalDetailsContactInformationResponse> getContactInformation(
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var informationResponse = rentalDetailsService.getContactInformation(language);
    return new ResponseEntity<>(informationResponse, HttpStatus.OK);
  }

  /**
   * Handle the /details endpoint.
   * @param language selected language.
   * @return rental details.
   */
  @GetMapping
  public ResponseEntity<RentalDetailsResponse> getRentalDetailsResponse(
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var informationResponse = rentalDetailsService.getRentalDetailsResponse(language);
    return new ResponseEntity<>(informationResponse, HttpStatus.OK);
  }

  /**
   * Handle the /details endpoint.
   * @param rentalDetailsUpdateRequest request with parameters.
   * @return status 200 if rental details successfully created or updated.
   */
  @PutMapping
  public ResponseEntity<HttpStatus> createOrUpdate(
      @Valid @RequestBody RentalDetailsUpdateRequest rentalDetailsUpdateRequest) {
    rentalDetailsService.createOrUpdate(rentalDetailsUpdateRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
