package com.example.carrental.controller.dto.location;

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
public class LocationNameResponse {

  @NotNull
  @Positive
  private long id;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String name;
}
