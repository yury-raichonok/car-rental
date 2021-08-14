package com.example.carrental.service.exceptions;

public class PassportNotConfirmedException extends RuntimeException {

  public PassportNotConfirmedException(String message) {
    super(message);
  }
}
