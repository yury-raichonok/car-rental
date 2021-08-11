package com.example.carrental.service.exceptions;

public class UsernameAlreadyTakenException extends Exception {

  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}
