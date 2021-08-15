package com.example.carrental.controller.dto.rentaldetails;

import com.example.carrental.entity.rentaldetails.RentalRequestType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDataRequest {

  @NotNull
  @Positive
  private long id;
  @NotNull
  private RentalRequestType requestType;
}