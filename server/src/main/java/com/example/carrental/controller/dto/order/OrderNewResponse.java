package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
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
 * Data transfer object for display new Orders.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNewResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Positive
  private long userId;
  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String userEmail;
  @NotNull
  @Size(
      min = 1,
      max = 61
  )
  private String carBrandModel;
  @NotNull
//  @Pattern(
//      regexp = "^[A-HJ-NPR-Za-hj-npr-z\\d]{8}[\\dX][A-HJ-NPR-Za-hj-npr-z\\d]{2}\\d{6}$"
//  )
  private String carVin;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String locationName;
  @NotNull
  @Future
  private String pickUpDate;
  @NotNull
  @Future
  private String returnDate;
  @NotNull
  @Min(0)
  private BigDecimal totalCost;
  @NotNull
  @PastOrPresent
  private String sentDate;
}
