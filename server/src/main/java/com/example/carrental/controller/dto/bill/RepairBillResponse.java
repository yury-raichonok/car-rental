package com.example.carrental.controller.dto.bill;

import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairBillResponse {

  @NotNull
  @Positive
  private long id;
  @Email
  private String userEmail;
  @NotNull
  @PastOrPresent
  private String sentDate;
  @NotNull
  @Min(0)
  private BigDecimal totalCost;
  private String message;
  private String paymentDate;
  @NotNull
  @Positive
  private long orderId;
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
}
