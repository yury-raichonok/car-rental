package com.example.carrental.service.exceptions;

public class CarAlreadyBookedException extends Exception {

  public CarAlreadyBookedException(String message) {
    super(message);
  }
}
