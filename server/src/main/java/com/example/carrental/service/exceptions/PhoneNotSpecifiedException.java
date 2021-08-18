package com.example.carrental.service.exceptions;

/**
 * PhoneNotSpecifiedException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when a user tries to create an order with not specified contact phone.
 * </p>
 * @author Yury Raichonak
 */
public class PhoneNotSpecifiedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public PhoneNotSpecifiedException(String message) {
    super(message);
  }
}
