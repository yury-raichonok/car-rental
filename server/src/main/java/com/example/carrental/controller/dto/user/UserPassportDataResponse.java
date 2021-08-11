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
public class UserPassportDataResponse {

  private String firstName;
  private String middleName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String passportSeries;
  private String passportNumber;
  private LocalDate dateOfIssue;
  private LocalDate validityPeriod;
  private String organizationThatIssued;
}
