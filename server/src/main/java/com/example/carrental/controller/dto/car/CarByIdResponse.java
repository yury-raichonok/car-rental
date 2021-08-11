package com.example.carrental.controller.dto.car;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarByIdResponse {

  private Long id;
  private String vin;
  private String brand;
  private String model;
  private String carClass;
  private String yearOfIssue;
  private String bodyType;
  private boolean isAutomaticTransmission;
  private String color;
  private String engineType;
  private int passengersAmt;
  private int baggageAmt;
  private boolean hasConditioner;
  private BigDecimal costPerHour;
  private BigDecimal costPerHourUpToWeek;
  private BigDecimal costPerHourMoreThanWeek;
  private String locationName;
  private double locationCoordinateX;
  private double locationCoordinateY;
  private String carImageLink;
}
