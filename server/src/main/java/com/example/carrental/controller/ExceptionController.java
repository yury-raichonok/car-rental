package com.example.carrental.controller;

import com.example.carrental.service.exceptions.CarAlreadyBookedException;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.NoContentException;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UserNotAuthenticatedException;
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
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(value = {NoContentException.class})
  public ResponseEntity<String> handleNoContentException(NoContentException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {DocumentsNotConfirmedException.class})
  public ResponseEntity<String> handleDocumentsNotConfirmedException(
      DocumentsNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {CarAlreadyBookedException.class})
  public ResponseEntity<String> handleCarAlreadyBookedException(CarAlreadyBookedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {FontNotFoundException.class})
  public ResponseEntity<String> handleFontNotFoundException(FontNotFoundException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {UsernameAlreadyTakenException.class})
  public ResponseEntity<String> handleUsernameAlreadyTakenException(
      UsernameAlreadyTakenException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {UserNotAuthenticatedException.class})
  public ResponseEntity<String> handleUserNotAuthenticatedException(
      UserNotAuthenticatedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = {MailSendException.class})
  public ResponseEntity<String> handleMailSendException(MailSendException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {TokenExpireException.class})
  public ResponseEntity<String> handleTokenExpireException(TokenExpireException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {DrivingLicenseNotConfirmedException.class})
  public ResponseEntity<String> handleDrivingLicenseNotConfirmedException(
      DrivingLicenseNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {DocumentNotGeneratedException.class})
  public ResponseEntity<String> handleDocumentNotGeneratedException(
      DocumentNotGeneratedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }
}
