package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderResponse {

  //TODO: add constraints
  private Long id;
  private String pickUpDate;
  private String returnDate;
  private BigDecimal totalCost;
  private String sentDate;
  private String carBrandModel;
  private String carVin;
  private String locationName;
}
