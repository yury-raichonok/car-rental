package com.example.carrental.controller.dto.rentalDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailsContactInformationResponse {

  private String email;
  private String phone;
  private String locationName;
  private double locationCoordinateX;
  private double locationCoordinateY;
  private int zoom;
}
