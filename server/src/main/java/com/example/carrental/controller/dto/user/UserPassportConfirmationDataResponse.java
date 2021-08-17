package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display User Passport confirmation data.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassportConfirmationDataResponse {

  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String firstName;
  private String middleName;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String lastName;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String dateOfBirth;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String passportSeries;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String passportNumber;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String dateOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private String validityPeriod;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String organizationThatIssued;
  private String documentsFileLink;
}
