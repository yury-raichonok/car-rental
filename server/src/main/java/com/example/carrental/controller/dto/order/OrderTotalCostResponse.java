package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display Order total cost.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTotalCostResponse {

  @NotNull
  @Min(0)
  private BigDecimal totalCost;
}
