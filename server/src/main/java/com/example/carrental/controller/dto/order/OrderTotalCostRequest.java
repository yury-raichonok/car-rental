package com.example.carrental.controller.dto.order;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTotalCostRequest {

  //TODO: add validation
  private double costPerHour;
  private LocalDateTime pickUpDate;
  private LocalDateTime returnDate;
}
