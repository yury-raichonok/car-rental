package com.example.carrental.controller;

import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  public ResponseEntity<String> handleEntityAlreadyExistsException (EntityAlreadyExistsException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(value = {NoContentException.class})
  public ResponseEntity<String> handleNoContentException(NoContentException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
  }

  public ResponseEntity<String> handleIllegalStateException (IllegalStateException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
