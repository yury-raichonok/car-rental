package com.example.carrental.service.exceptions;

/**
 * OrderPeriodValidationException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown if user specified invalid rental period.
 * </p>
 * @author Yury Raichonak
 */
public class OrderPeriodValidationException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public OrderPeriodValidationException(String message) {
  }
}
