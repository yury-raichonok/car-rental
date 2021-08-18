package com.example.carrental.service.exceptions;

/**
 * DrivingLicenseNotConfirmedException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when Administrator tries to change status of not confirmed driving license.
 * </p>
 * @author Yury Raichonak
 */
public class DrivingLicenseNotConfirmedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public DrivingLicenseNotConfirmedException(String message) {
    super(message);
  }
}
