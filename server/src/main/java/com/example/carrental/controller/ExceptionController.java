package com.example.carrental.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.NoContentException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PassportNotConfirmedException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The controller for handling thrown exceptions and send the appropriate response message and
 * status.
 *
 * @author Yury Raichonak
 */
@ControllerAdvice
public class ExceptionController {

  /**
   * @param e - EntityAlreadyExistsException.
   * @return response with message and code 406.
   */
  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  public ResponseEntity<String> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NOT_ACCEPTABLE);
  }

  /**
   * @param e - NoContentException.
   * @return response with message and code 204.
   */
  @ExceptionHandler(value = {NoContentException.class})
  public ResponseEntity<String> handleNoContentException(NoContentException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NO_CONTENT);
  }

  /**
   * @param e - IllegalStateException.
   * @return response with message and code 400.
   */
  @ExceptionHandler(value = {IllegalStateException.class})
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), BAD_REQUEST);
  }

  /**
   * @param e - DocumentsNotConfirmedException.
   * @return response with message and code 400.
   */
  @ExceptionHandler(value = {DocumentsNotConfirmedException.class})
  public ResponseEntity<String> handleDocumentsNotConfirmedException(
      DocumentsNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), BAD_REQUEST);
  }

  /**
   * @param e - FontNotFoundException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {FontNotFoundException.class})
  public ResponseEntity<String> handleFontNotFoundException(FontNotFoundException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  /**
   * @param e - UsernameAlreadyTakenException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {UsernameAlreadyTakenException.class})
  public ResponseEntity<String> handleUsernameAlreadyTakenException(
      UsernameAlreadyTakenException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  /**
   * @param e - MailSendException.
   * @return response with message and code 404.
   */
  @ExceptionHandler(value = {MailSendException.class})
  public ResponseEntity<String> handleMailSendException(MailSendException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NOT_FOUND);
  }

  /**
   * @param e - TokenExpireException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {TokenExpireException.class})
  public ResponseEntity<String> handleTokenExpireException(TokenExpireException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  /**
   * @param e - DrivingLicenseNotConfirmedException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {DrivingLicenseNotConfirmedException.class})
  public ResponseEntity<String> handleDrivingLicenseNotConfirmedException(
      DrivingLicenseNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  /**
   * @param e - DocumentNotGeneratedException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {DocumentNotGeneratedException.class})
  public ResponseEntity<String> handleDocumentNotGeneratedException(
      DocumentNotGeneratedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }

  /**
   * @param e - OrderPeriodValidationException.
   * @return response with message and code 406.
   */
  @ExceptionHandler(value = {OrderPeriodValidationException.class})
  public ResponseEntity<String> handleOrderPeriodValidationException(
      OrderPeriodValidationException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), NOT_ACCEPTABLE);
  }

  /**
   * @param e - PhoneNotSpecifiedException
   * @return response with message and code 403.
   */
  @ExceptionHandler(value = {PhoneNotSpecifiedException.class})
  public ResponseEntity<String> handlePhoneNotSpecifiedException(PhoneNotSpecifiedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), FORBIDDEN);
  }

  /**
   * @param e - PassportNotConfirmedException.
   * @return response with message and code 405.
   */
  @ExceptionHandler(value = {PassportNotConfirmedException.class})
  public ResponseEntity<String> handlePassportNotConfirmedException(
      PassportNotConfirmedException e) {
    return new ResponseEntity<>(e.getLocalizedMessage(), METHOD_NOT_ALLOWED);
  }
}
