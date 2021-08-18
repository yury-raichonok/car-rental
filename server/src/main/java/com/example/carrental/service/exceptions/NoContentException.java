package com.example.carrental.service.exceptions;

/**
 * NoContentException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown if the received data list is empty.
 * </p>
 * @author Yury Raichonak
 */
public class NoContentException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public NoContentException(String message) {
    super(message);
  }
}
