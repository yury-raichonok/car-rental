package com.example.carrental.controller.dto.rentalDetails;

import com.example.carrental.entity.rentalDetails.RentalRequestType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequestRequest {

  @NotNull
  private RentalRequestType rentalRequestType;
}
