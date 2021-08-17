package com.example.carrental.service.exceptions;

///**
// * Custom Runtime Exception.
// * <p>
// * An exception is thrown when a user tries to create an order with unconfirmed documents.
// * </p>
// * @author Yury Raichonak
// */
public class DrivingLicenseNotConfirmedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public DrivingLicenseNotConfirmedException(String message) {
    super(message);
  }
}
