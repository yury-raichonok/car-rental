package com.example.carrental.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDrivingLicenseConfirmationDataResponse {

  private String dateOfIssue;
  private String validityPeriod;
  private String organizationThatIssued;
  private String documentsFileLink;
}
