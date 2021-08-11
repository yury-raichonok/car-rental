package com.example.carrental.controller.dto.order;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNewResponse {

  private Long id;
  private Long userId;
  private String userEmail;
  private String carBrandModel;
  private String carVin;
  private String locationName;
  private String pickUpDate;
  private String returnDate;
  private BigDecimal totalCost;
  private String sentDate;
}
