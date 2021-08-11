package com.example.carrental.controller.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationWithTranslationsResponse {

  private Long id;
  private String nameEn;
  private String nameRu;
  private String nameBe;
  private double coordinateX;
  private double coordinateY;
  private int zoom;
}
