package com.example.carrental.service.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {

  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}
