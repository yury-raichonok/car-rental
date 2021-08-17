package com.example.carrental.controller.dto.rentaldetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display Rental contact information.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailsContactInformationResponse {

  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  @NotNull
  @Pattern(regexp = "^\\+375[0-9]{9}$")
  private String phone;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String locationName;
  @NotNull
  @Positive
  private double locationCoordinateX;
  @NotNull
  @Positive
  private double locationCoordinateY;
  @NotNull
  @Positive
  private int zoom;
}
