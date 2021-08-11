package com.example.carrental.controller.dto.user;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDrivingLicenseDataResponse {

  private LocalDate dateOfIssue;
  private LocalDate validityPeriod;
  private String organizationThatIssued;
}
