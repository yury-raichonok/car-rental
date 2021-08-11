package com.example.carrental.controller.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest {

  private int pageNumber = 0;
  private int pageSize = 10;
  private String sortDirection = "asc";
  private String sortBy = "id";
  private String email;
  private String carVin;
}
