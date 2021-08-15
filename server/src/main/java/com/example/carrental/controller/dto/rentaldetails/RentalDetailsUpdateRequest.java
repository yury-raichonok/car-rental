package com.example.carrental.controller.dto.rentaldetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailsUpdateRequest {

  @Pattern(regexp = "^\\+375[0-9]{9}$")
  private String phoneNumber;
  @NotNull
  @Positive
  private Long location;
}
