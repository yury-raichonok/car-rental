package com.example.carrental.service.exceptions;

public class TokenExpireException extends Exception {

  public TokenExpireException(String message) {
    super(message);
  }
}
