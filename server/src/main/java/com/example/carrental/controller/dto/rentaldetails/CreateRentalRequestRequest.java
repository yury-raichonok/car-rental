package com.example.carrental.controller.dto.rentaldetails;

import com.example.carrental.entity.rentaldetails.RentalRequestType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating Rental Request.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequestRequest {

  @NotNull
  private RentalRequestType rentalRequestType;
}
