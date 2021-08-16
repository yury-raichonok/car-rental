package com.example.carrental.controller.dto.order;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  private LocalDateTime pickUpDate;
  private LocalDateTime returnDate;
  @NotNull
  private double totalCost;
  @NotNull
  private long carId;
}
