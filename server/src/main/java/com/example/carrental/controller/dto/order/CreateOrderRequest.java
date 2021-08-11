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
public class CreateOrderRequest {

  //TODO: add validation
  private LocalDateTime pickUpDate;
  private LocalDateTime returnDate;
  private double totalCost;
  private long carId;
}
