package com.example.carrental.controller.dto.bill;

import com.example.carrental.entity.order.Order;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating new Repair Bill for specified Order.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepairBillRequest {

  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String message;
  @NotNull
  @Min(0)
  private BigDecimal totalCost;
  @NotNull
  @Positive
  private Order order;
}
