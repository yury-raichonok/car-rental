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

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/phone")
@Validated
public class UserPhoneController {

  private final UserPhoneService userPhoneService;

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody UserPhoneConfirmationRequest userPhoneConfirmationRequest)
      throws TokenExpireException, EntityAlreadyExistsException {
    userPhoneService.create(userPhoneConfirmationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<HttpStatus> sendConfirmationSms(
      @Valid @RequestBody UserSmsRequest userSmsRequest) throws EntityAlreadyExistsException {
    userPhoneService.sendConfirmationSms(userSmsRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> updatePhoneStatus(@NotNull @Positive @PathVariable Long id)
      throws EntityAlreadyExistsException {
    userPhoneService.updatePhoneStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
