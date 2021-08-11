package com.example.carrental.controller.dto.rentalDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalUserDetailsStatisticResponse {

  private int paymentBills;
  private int repairBills;
  private int orders;
  private int notifications;
}
