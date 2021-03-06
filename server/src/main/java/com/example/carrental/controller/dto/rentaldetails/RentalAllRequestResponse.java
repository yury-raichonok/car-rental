package com.example.carrental.controller.dto.rentaldetails;

import com.example.carrental.entity.rentaldetails.RentalRequestType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display all Rental Requests.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalAllRequestResponse {

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
  @PastOrPresent
  private String sentDate;
  @NotNull
  private boolean considered;
  private String considerationDate;
  private String comments;
}
