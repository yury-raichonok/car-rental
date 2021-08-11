package com.example.carrental.controller.dto.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarModelBrandNameResponse {

  private Long id;
  private String name;
  private String brand;
}
