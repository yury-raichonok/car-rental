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
public class CarResponse {

  private Long id;
  private String brand;
  private String model;
  private String vin;
  private String location;
  private String carClass;
  private LocalDate dateOfIssue;
  private String color;
  private String bodyType;
  private String transmission;
  private String engineType;
  private int passengersAmt;
  private int baggageAmt;
  private String hasConditioner;
  private BigDecimal cost;
  private String carFileLink;
}
