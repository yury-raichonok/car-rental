package com.example.carrental.controller.dto.rentalDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalAdminDetailsStatisticResponse {

  private int messagesAmt;
  private int requests;
  private int orders;
}
