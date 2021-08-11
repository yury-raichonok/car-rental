package com.example.carrental.controller.dto.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompleteWithPenaltyRequest {

  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String message;
  @NotNull
  @Min(0)
  private double totalCost;
}
