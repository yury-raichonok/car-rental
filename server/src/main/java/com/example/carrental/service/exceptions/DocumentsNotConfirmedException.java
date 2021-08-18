package com.example.carrental.service.exceptions;

/**
 * DocumentsNotConfirmedException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when a user tries to create an order with unconfirmed documents.
 * </p>
 * @author Yury Raichonak
 */
public class DocumentsNotConfirmedException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public DocumentsNotConfirmedException(String message) {
    super(message);
  }
}
