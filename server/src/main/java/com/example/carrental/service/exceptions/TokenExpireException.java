package com.example.carrental.service.exceptions;

public class TokenExpireException extends RuntimeException {

  public TokenExpireException(String message) {
    super(message);
  }
}
