package com.example.carrental.controller.dto.location;

import javax.validation.constraints.Min;
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
public class LocationWithTranslationsResponse {

  @NotNull
  @Positive
  private long id;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String nameEn;
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
  @NotNull
  @Min(0)
  private double coordinateX;
  @NotNull
  @Min(0)
  private double coordinateY;
  @NotNull
  @Min(0)
  private int zoom;
}
