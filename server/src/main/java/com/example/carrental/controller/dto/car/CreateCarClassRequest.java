package com.example.carrental.controller.dto.car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating Car Class.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarClassRequest {

  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String nameRu;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String nameBe;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String nameEn;
}
