package com.example.carrental.controller.dto.rentaldetails;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for rejecting Rental Requests.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestRejectRequest {

  @Size(
      min = 1,
      max = 255
  )
  private String comments;
}
