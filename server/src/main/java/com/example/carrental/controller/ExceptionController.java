package com.example.carrental.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.NoContentException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  public ResponseEntity<String> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NOT_ACCEPTABLE);
  }

  @ExceptionHandler(value = {NoContentException.class})
  public ResponseEntity<String> handleNoContentException(NoContentException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NO_CONTENT);
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(value = {DocumentsNotConfirmedException.class})
  public ResponseEntity<String> handleDocumentsNotConfirmedException(
      DocumentsNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(value = {FontNotFoundException.class})
  public ResponseEntity<String> handleFontNotFoundException(FontNotFoundException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {UsernameAlreadyTakenException.class})
  public ResponseEntity<String> handleUsernameAlreadyTakenException(
      UsernameAlreadyTakenException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {MailSendException.class})
  public ResponseEntity<String> handleMailSendException(MailSendException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {TokenExpireException.class})
  public ResponseEntity<String> handleTokenExpireException(TokenExpireException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {DrivingLicenseNotConfirmedException.class})
  public ResponseEntity<String> handleDrivingLicenseNotConfirmedException(
      DrivingLicenseNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {DocumentNotGeneratedException.class})
  public ResponseEntity<String> handleDocumentNotGeneratedException(
      DocumentNotGeneratedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {OrderPeriodValidationException.class})
  public ResponseEntity<String> handleOrderPeriodValidationException(
      OrderPeriodValidationException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NOT_ACCEPTABLE);
  }

  @ExceptionHandler(value = {PhoneNotSpecifiedException.class})
  public ResponseEntity<String> handlePhoneNotSpecifiedException(PhoneNotSpecifiedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), FORBIDDEN);
  }
}
