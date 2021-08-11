package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
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
public class OrderResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String userEmail;
  @NotNull
  private String pickUpDate;
  @NotNull
  private String returnDate;
  @NotNull
  @Min(0)
  private BigDecimal totalCost;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String paymentStatus;
  private String comments;
  @NotNull
  @Past
  private String sentDate;
  private String paymentDate;
  private String denyingDate;
  private long paymentBillId;
  private long repairBillId;
  @NotNull
  @Size(
      min = 1,
      max = 30
  )
  private String rentalStatus;
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
