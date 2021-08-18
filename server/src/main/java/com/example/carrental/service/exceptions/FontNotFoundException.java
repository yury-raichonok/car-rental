package com.example.carrental.service.exceptions;

/**
 * FontNotFoundException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when usr tries to export order to PDF and application can not found font path.
 * </p>
 * @author Yury Raichonak
 */
public class FontNotFoundException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public FontNotFoundException(String message) {
    super(message);
  }
}
