package com.example.carrental.controller.dto.order;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for calculating Order total cost.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTotalCostRequest {

  @NotNull
  @Min(0)
  private double costPerHour;
  @NotNull
  private LocalDateTime pickUpDate;
  @NotNull
  private LocalDateTime returnDate;
}
