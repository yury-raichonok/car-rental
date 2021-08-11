package com.example.carrental.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassportConfirmationDataResponse {

  private String firstName;
  private String middleName;
  private String lastName;
  private String dateOfBirth;
  private String passportSeries;
  private String passportNumber;
  private String dateOfIssue;
  private String validityPeriod;
  private String organizationThatIssued;
  private String documentsFileLink;
}
