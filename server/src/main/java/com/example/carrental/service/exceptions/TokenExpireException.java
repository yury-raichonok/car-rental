package com.example.carrental.service.exceptions;

/**
 * TokenExpireException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when using expired User Confirmation Token.
 * </p>
 * @author Yury Raichonak
 */
public class TokenExpireException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public TokenExpireException(String message) {
    super(message);
  }
}
