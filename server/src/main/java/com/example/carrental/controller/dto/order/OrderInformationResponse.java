package com.example.carrental.controller.dto.order;

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
public class OrderInformationResponse {

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
  @NotNull
  @PastOrPresent
  private String sentDate;
  private String paymentDate;
  private long paymentBillId;
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
