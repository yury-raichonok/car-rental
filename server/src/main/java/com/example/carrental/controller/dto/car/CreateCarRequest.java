package com.example.carrental.controller.dto.car;

import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarEngineType;
import java.math.BigDecimal;
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
public class CreateCarRequest {

  @NotNull
  @Positive
  private Long brandId;
  @NotNull
  @Positive
  private Long modelId;
  @NotNull
//  @Pattern(
//      regexp = "^[A-HJ-NPR-Za-hj-npr-z\\d]{8}[\\dX][A-HJ-NPR-Za-hj-npr-z\\d]{2}\\d{6}$"
//  )
  private String vin;
  @NotNull
  @Positive
  private Long locationId;
  @NotNull
  @Positive
  private Long carClassId;
  @NotNull
  @PastOrPresent
  private LocalDate dateOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String color;
  @NotNull
  private CarBodyType bodyType;
  @NotNull
  private CarEngineType engineType;
  @NotNull
  @Positive
  @Max(10)
  private int passengersAmt;
  @Positive
  @Max(10)
  private int baggageAmt;
  @NotNull
  private boolean autoTransmission;
  @NotNull
  private boolean hasConditioner;
  @NotNull
  private boolean isInRental;
  @NotNull
  @Min(0)
  private BigDecimal costPerHour;
}
