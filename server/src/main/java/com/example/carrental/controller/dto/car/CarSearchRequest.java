package com.example.carrental.controller.dto.car;

import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarEngineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchRequest {

  private int pageNumber = 0;
  private int pageSize = 10;
  private String sortDirection = "asc";
  private String sortBy = "costPerHour";
  //TODO: add validation constraints
  private String brandName;
  private String modelName;
  private Long location;
  private String carClassName;
  private String vin;
  private String costFrom;
  private String costTo;
  private String yearFrom;
  private String yearTo;
  private CarBodyType bodyType;
  private CarEngineType engineType;
  private boolean airConditioner;
  private boolean autoTransmission;
  private boolean inRental;
}
