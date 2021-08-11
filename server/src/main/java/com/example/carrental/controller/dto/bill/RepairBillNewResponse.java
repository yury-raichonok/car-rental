package com.example.carrental.controller.dto.bill;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairBillNewResponse {

  private Long id;
  private String sentDate;
  private BigDecimal totalCost;
  private String message;
  private Long orderId;
  private String carBrandModel;
  private String carVin;
  private String locationName;
}
