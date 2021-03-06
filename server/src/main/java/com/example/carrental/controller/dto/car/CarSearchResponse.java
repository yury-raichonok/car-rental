package com.example.carrental.controller.dto.car;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display Cars.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchResponse {

  @NotNull
  @Positive
  private long id;
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
  @Size(
      min = 4,
      max = 4
  )
  private String yearOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 10
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
  private BigDecimal costPerHour;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String locationName;
  private String carImageLink;
}
