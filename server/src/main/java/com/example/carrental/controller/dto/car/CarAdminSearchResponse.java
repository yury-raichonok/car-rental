package com.example.carrental.controller.dto.car;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAdminSearchResponse {

  private Long id;
  private String vin;
  private String brand;
  private String model;
  private String carClass;
  private Long carClassId;
  private LocalDate dateOfIssue;
  private String bodyType;
  private boolean isAutomaticTransmission;
  private String color;
  private String engineType;
  private int passengersAmt;
  private int baggageAmt;
  private boolean hasConditioner;
  private double costPerHour;
  private String locationName;
  private Long locationId;
  private boolean isInRental;
  private String carImageLink;
}
