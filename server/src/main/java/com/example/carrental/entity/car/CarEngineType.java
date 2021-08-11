package com.example.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarEngineType {

  DIESEL("Diesel"),
  PETROL("Petrol"),
  HYBRID("Hybrid"),
  ELECTRO("Electro");

  private final String engineType;
}
