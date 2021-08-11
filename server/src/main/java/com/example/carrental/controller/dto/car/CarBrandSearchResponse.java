package com.example.carrental.controller.dto.car;

import javax.validation.constraints.NotNull;
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
public class CarBrandSearchResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Size(
      min = 1,
      max = 30
  )
  private String name;
  private String imageLink;
}
