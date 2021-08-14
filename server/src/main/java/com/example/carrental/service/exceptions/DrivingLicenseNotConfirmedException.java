package com.example.carrental.service.exceptions;

public class DrivingLicenseNotConfirmedException extends RuntimeException {

  public DrivingLicenseNotConfirmedException(String message) {
    super(message);
  }
}
