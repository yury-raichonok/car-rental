package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display user Orders.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  private String pickUpDate;
  @NotNull
  private String returnDate;
  @NotNull
  @Min(0)
  private BigDecimal totalCost;
  @NotNull
  @PastOrPresent
  private String sentDate;
  @NotNull
  @Size(
      min = 1,
      max = 61
  )
  private String carBrandModel;
  @NotNull
  @Size(
      min = 1,
      max = 61
  )
  private String carVin;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String locationName;
}
