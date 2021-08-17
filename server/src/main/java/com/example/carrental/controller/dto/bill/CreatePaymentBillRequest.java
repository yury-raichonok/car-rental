package com.example.carrental.controller.dto.bill;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating new Payment Bill for specified Order.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentBillRequest {

  @NotNull
  @Positive
  private long orderId;
}
