package com.example.carrental.controller.dto.order;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @NotNull
  @Future
  private LocalDateTime pickUpDate;
  @Future
  private LocalDateTime returnDate;
  @NotNull
  @Min(0)
  private double totalCost;
  @NotNull
  @Positive
  private long carId;
}
