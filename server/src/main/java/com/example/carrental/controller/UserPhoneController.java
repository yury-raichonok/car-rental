package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.service.UserPhoneService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.TokenExpireException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for Phones REST endpoints.
 * <p>
 * This class handles the CRUD operations for Phones, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/phone")
@Validated
public class UserPhoneController {

  private final UserPhoneService userPhoneService;

  /**
   * Handle the /users/phone endpoint.
   * @param userPhoneConfirmationRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new phone.
   * 405: unable to created new phone, because confirmation token already expired.
   * 406: unable to created new phone, because such phone number already exists.
   * @throws TokenExpireException if confirmation token already expired.
   * @throws EntityAlreadyExistsException if such phone number already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody UserPhoneConfirmationRequest userPhoneConfirmationRequest)
      throws TokenExpireException, EntityAlreadyExistsException {
    userPhoneService.create(userPhoneConfirmationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /users/phone endpoint.
   * @param userSmsRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully send SMS for phone confirmation.
   * 406: unable to send confirmation SMS, because such phone number already exists.
   * @throws EntityAlreadyExistsException if such phone number already exists.
   */
  @PutMapping
  public ResponseEntity<HttpStatus> sendConfirmationSms(
      @Valid @RequestBody UserSmsRequest userSmsRequest) throws EntityAlreadyExistsException {
    userPhoneService.sendConfirmationSms(userSmsRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /users/phone/{id} endpoint.
   * @param id
   * Return one of the following status codes:
   * 200: phone status successfully updated.
   * 406: unable to update phone status, because such phone number already exists.
   * @throws EntityAlreadyExistsException if such phone number already exists.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> updatePhoneStatus(@NotNull @Positive @PathVariable Long id)
      throws EntityAlreadyExistsException {
    userPhoneService.updatePhoneStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
