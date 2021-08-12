package com.example.carrental.service.exceptions;

public class UserNotAuthenticatedException extends Exception {

  public UserNotAuthenticatedException(String message) {
    super(message);
  }
}
