package com.example.carrental.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarBodyType {

  HATCHBACK("Hatchback"),
  SEDAN("Sedan"),
  MUV("MUV"),
  COUPE("Coupe"),
  CONVERTIBLE("Convertible"),
  WAGON("Wagon"),
  VAN("Van"),
  JEEP("Jeep");

  private final String bodyType;
}
