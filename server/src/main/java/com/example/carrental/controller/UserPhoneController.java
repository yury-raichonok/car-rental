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
import org.springframework.web.bind.annotation.CrossOrigin;
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

  @PutMapping
  public ResponseEntity<String> sendConfirmationSms(
      @Valid @RequestBody UserSmsRequest userSmsRequest) {
    try {
      var response = userPhoneService.sendConfirmationSms(userSmsRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody UserPhoneConfirmationRequest userPhoneConfirmationRequest) {
    try {
      var response = userPhoneService.create(userPhoneConfirmationRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (TokenExpireException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> updatePhoneStatus(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = userPhoneService.updatePhoneStatus(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }
}
