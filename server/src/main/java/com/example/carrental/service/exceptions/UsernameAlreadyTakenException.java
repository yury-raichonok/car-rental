package com.example.carrental.service.exceptions;

/**
 * UsernameAlreadyTakenException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when user tries to register with existing email.
 * </p>
 * @author Yury Raichonak
 */
public class UsernameAlreadyTakenException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}
