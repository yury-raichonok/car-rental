package com.example.carrental.controller.dto.rentalDetails;

import com.example.carrental.entity.rentalDetails.RentalRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalAllRequestResponse {

  private Long id;
  private String userEmail;
  private RentalRequestType requestType;
  private String sentDate;
  private boolean considered;
  private String considerationDate;
  private String comments;
}
