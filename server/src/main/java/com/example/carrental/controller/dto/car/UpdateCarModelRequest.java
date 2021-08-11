package com.example.carrental.controller.dto.car;

import javax.validation.constraints.NotBlank;
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
public class UpdateCarModelRequest {

  @NotBlank
  @Size(
      min = 1,
      max = 30
  )
  private String brand;
  @NotBlank
  @Size(
      min = 1,
      max = 30
  )
  private String name;
}
