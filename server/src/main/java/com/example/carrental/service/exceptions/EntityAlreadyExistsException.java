package com.example.carrental.service.exceptions;

/**
 * EntityAlreadyExistsException it is a custom Runtime Exception.
 * <p>
 * An exception is thrown when Administrator tries to create new entity with existing unique parameters.
 * </p>
 * @author Yury Raichonak
 */
public class EntityAlreadyExistsException extends RuntimeException {

  /**
   * @param message of exception.
   */
  public EntityAlreadyExistsException(String message) {
    super(message);
  }
}
