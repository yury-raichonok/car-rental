package com.example.carrental.controller.dto.car;

import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
public class CarAdminSearchResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
//  @Pattern(
//      regexp = "^[A-HJ-NPR-Za-hj-npr-z\\d]{8}[\\dX][A-HJ-NPR-Za-hj-npr-z\\d]{2}\\d{6}$"
//  )
  private String vin;
  @NotNull
  @Size(
      min = 1,
      max = 30
  )
  private String brand;
  @NotNull
  @Size(
      min = 1,
      max = 30
  )
  private String model;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String carClass;
  @NotNull
  @Positive
  private long carClassId;
  @NotNull
  @PastOrPresent
  private LocalDate dateOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String bodyType;
  @NotNull
  private boolean isAutomaticTransmission;
  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String color;
  @NotNull
  @Size(
      min = 1,
      max = 15
  )
  private String engineType;
  @NotNull
  @Positive
  @Max(10)
  private int passengersAmt;
  @NotNull
  @Positive
  @Max(10)
  private int baggageAmt;
  @NotNull
  private boolean hasConditioner;
  @NotNull
  @Min(0)
  private double costPerHour;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String locationName;
  @NotNull
  @Positive
  private long locationId;
  @NotNull
  private boolean isInRental;
  private String carImageLink;
}
