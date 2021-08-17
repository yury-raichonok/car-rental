package com.example.carrental.controller.dto.car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating Car Brand.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarBrandRequest {

  @NotBlank
  @Size(
      min = 1,
      max = 30
  )
  private String name;
}
