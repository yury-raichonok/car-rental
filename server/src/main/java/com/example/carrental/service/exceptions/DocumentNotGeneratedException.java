package com.example.carrental.service.exceptions;

public class DocumentNotGeneratedException extends RuntimeException {

  public DocumentNotGeneratedException(String message) {
    super(message);
  }
}
