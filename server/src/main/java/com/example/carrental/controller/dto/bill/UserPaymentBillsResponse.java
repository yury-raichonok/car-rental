package com.example.carrental.controller.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentBillsResponse {

  private Long id;
  private String sentDate;
  private double totalCost;
  private String paymentDate;
  private Long orderId;
  private String carBrandModel;
  private String carVin;
  private String locationName;
}
