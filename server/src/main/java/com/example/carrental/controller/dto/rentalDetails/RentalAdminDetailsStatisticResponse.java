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
public class RentalAdminDetailsStatisticResponse {

  @NotNull
  private int messagesAmt;
  @NotNull
  private int requests;
  @NotNull
  private int orders;
}
