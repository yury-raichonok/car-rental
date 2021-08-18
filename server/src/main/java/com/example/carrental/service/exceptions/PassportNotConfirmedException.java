package com.example.carrental.service.exceptions;

/**
 * PassportNotConfirmedException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when Administrator tries to change status of not confirmed user passport.
 * </p>
 * @author Yury Raichonak
 */
public class PassportNotConfirmedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public PassportNotConfirmedException(String message) {
    super(message);
  }
}
