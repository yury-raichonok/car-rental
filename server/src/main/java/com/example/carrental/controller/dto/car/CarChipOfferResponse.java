package com.example.carrental.controller.dto.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarChipOfferResponse {

  private Long id;
  private String brand;
  private String model;
  private String carClass;
  private int amountOfSeats;
  private int amountOfBaggage;
  private boolean autoTransmission;
  private boolean airConditioner;
  private String carImageLink;
  private String costPerHour;
}
