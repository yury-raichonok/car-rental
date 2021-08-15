package com.example.carrental.controller.dto.order;

import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTotalCostRequest {

  @NotNull
  @Min(0)
  private double costPerHour;
  @NotNull
  @FutureOrPresent
  private LocalDateTime pickUpDate;
  @NotNull
  @FutureOrPresent
  private LocalDateTime returnDate;
}
