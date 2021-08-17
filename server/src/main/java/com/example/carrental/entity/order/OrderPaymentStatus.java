package com.example.carrental.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents Order payment status.
 *
 * @author Yury Raichonak
 */
@Getter
@AllArgsConstructor
public enum OrderPaymentStatus {

  PAID("Paid"),
  NOT_PAID("Not paid");

  private final String status;
}
