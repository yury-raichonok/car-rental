package com.example.carrental.controller.dto.rentalDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailsAndStatisticResponse {

  private String email;
  private String phoneNumber;
  private long locationId;
  private String location;
  private double fromDayToWeekCoefficient;
  private double fromWeekCoefficient;
  private int billValidityPeriod;
  private int newMessages;
  private int newOrders;
  private int newUsers;
  private int newRequests;
}
