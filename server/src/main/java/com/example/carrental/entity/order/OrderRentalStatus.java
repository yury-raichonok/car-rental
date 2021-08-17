package com.example.carrental.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents Order rental status.
 *
 * @author Yury Raichonak
 */
@Getter
@AllArgsConstructor
public enum OrderRentalStatus {

  NEW("New"),
  DENIED("Denied"),
  CANCELED("Canceled"),
  NOT_STARTED("Not started"),
  IN_PROCESS("In process"),
  FINISHED("Finished"),
  FINISHED_WITH_PENALTY("Finished with penalty");

  private final String status;
}
