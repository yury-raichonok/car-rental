package com.example.carrental.controller.dto.car;

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
 * Data transfer object for display Car profitable offers.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarProfitableOfferResponse {

  @NotNull
  @Positive
  private Long id;
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
  private int amountOfSeats;
  @NotNull
  @Positive
  @Max(10)
  private int amountOfBaggage;
  @NotNull
  private boolean autoTransmission;
  @NotNull
  private boolean airConditioner;
  private String carImageLink;
  @NotNull
  @Min(0)
  private String costPerHour;
}
