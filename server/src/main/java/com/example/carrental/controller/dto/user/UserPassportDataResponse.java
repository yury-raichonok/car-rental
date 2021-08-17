package com.example.carrental.controller.dto.user;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display User Passport data.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassportDataResponse {

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
  private LocalDate dateOfBirth;
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
  private LocalDate dateOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private LocalDate validityPeriod;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String organizationThatIssued;
}
