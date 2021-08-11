package com.example.carrental.controller.dto.rentalDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailsAndStatisticResponse {

  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  @NotNull
  @Pattern(regexp = "^\\+375[0-9]{9}$")
  private String phoneNumber;
  @NotNull
  @Positive
  private long locationId;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String location;
  @NotNull
  @Min(0)
  private double fromDayToWeekCoefficient;
  @NotNull
  @Min(0)
  private double fromWeekCoefficient;
  @NotNull
  @Min(0)
  private int billValidityPeriod;
  @NotNull
  private int newMessages;
  @NotNull
  private int newOrders;
  @NotNull
  private int newUsers;
  @NotNull
  private int newRequests;
}
