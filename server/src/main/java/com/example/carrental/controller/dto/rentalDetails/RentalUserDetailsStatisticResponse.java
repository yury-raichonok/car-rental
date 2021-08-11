package com.example.carrental.controller.dto.rentalDetails;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalUserDetailsStatisticResponse {

  @NotNull
  private int paymentBills;
  @NotNull
  private int repairBills;
  @NotNull
  private int orders;
  @NotNull
  private int notifications;
}
