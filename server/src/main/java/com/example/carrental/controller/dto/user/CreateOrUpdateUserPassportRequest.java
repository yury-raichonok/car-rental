package com.example.carrental.controller.dto.user;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateUserPassportRequest {

  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String firstName;
  private String middleName;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String lastName;
  @NotNull
  @PastOrPresent
  private LocalDate dateOfBirth;
  @NotBlank
  @Size(
      min = 1,
      max = 10
  )
  private String passportSeries;
  @NotBlank
  @Size(
      min = 1,
      max = 10
  )
  private String passportNumber;
  @NotNull
  @PastOrPresent
  private LocalDate dateOfIssue;
  @NotNull
  @Future
  private LocalDate validityPeriod;
  @NotBlank
  @Size(
      min = 1,
      max = 255
  )
  private String organizationThatIssued;
}
