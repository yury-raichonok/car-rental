package com.example.carrental.controller.dto.bill;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentBillsRequest {

  @NotNull
  @Min(0)
  private int pageNumber = 0;
  @NotNull
  @Min(10)
  private int pageSize = 10;
  @NotNull
  @Size(
      min = 3,
      max = 3
  )
  private String sortDirection = "asc";
  @NotNull
  private String sortBy = "id";
  @Email
  private String email;
}
