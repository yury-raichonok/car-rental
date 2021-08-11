package com.example.carrental.entity.rentalDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentalRequestType {

  DRIVING_LICENSE_CONFIRMATION_REQUEST("Driving license confirmation request"),
  PASSPORT_CONFIRMATION_REQUEST("Passport confirmation request");

  private final String requestType;
}
