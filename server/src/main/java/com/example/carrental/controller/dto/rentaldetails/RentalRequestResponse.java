package com.example.carrental.controller.dto.rentaldetails;

import com.example.carrental.entity.rentaldetails.RentalRequestType;
import javax.validation.constraints.Email;
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
public class RentalRequestResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String userEmail;
  @NotNull
  private RentalRequestType requestType;
  @NotNull
  private String sentDate;
  @NotNull
  private boolean considered;
  private String comments;
}
