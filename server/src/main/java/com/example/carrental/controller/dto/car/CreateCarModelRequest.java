package com.example.carrental.controller.dto.car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating Car Model.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarModelRequest {

  @NotNull
  @Positive
  private long brandId;
  @NotBlank
  @Size(
      min = 1,
      max = 30
  )
  private String name;
}
