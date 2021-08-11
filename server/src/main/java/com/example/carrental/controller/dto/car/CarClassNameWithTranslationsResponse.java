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
public class CarClassNameWithTranslationsResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String nameEn;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String nameRu;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String nameBe;
}
