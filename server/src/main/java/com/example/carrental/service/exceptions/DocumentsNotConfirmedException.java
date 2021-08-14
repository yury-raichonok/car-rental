package com.example.carrental.service.exceptions;

public class DocumentsNotConfirmedException extends RuntimeException {

  public DocumentsNotConfirmedException(String message) {
    super(message);
  }
}
