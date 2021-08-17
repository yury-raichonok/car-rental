package com.example.carrental.service.exceptions;

/**
 * Custom Runtime Exception.
 * <p>
 * The exception is thrown if the PDF document based on the order is not generated.
 * </p>
 * @author Yury Raichonak
 */
public class DocumentNotGeneratedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public DocumentNotGeneratedException(String message) {
    super(message);
  }
}
