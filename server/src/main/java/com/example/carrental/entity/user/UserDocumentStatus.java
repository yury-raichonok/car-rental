package com.example.carrental.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents User Document statuses.
 *
 * @author Yury Raichonak
 */
@Getter
@AllArgsConstructor
public enum  UserDocumentStatus {

  CONFIRMED("Confirmed"),
  UNDER_CONSIDERATION("Under consideration"),
  NOT_CONFIRMED("Not confirmed");

  private final String status;
}
