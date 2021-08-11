package com.example.carrental.controller.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {

  private Long id;
  private String name;
  private double coordinateX;
  private double coordinateY;
  private int zoom;
}
