package com.example.carrental.controller.dto.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Order search request.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest {

  @NotNull
  @Min(0)
  private int pageNumber = 0;
  @NotNull
  @Min(10)
  private int pageSize = 10;
  @NotNull
  @Size(
      min = 3,
      max = 4
  )
  private String sortDirection = "asc";
  @NotNull
  private String sortBy = "id";
  private String email;
  private String carVin;
}
