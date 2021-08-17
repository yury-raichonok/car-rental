package com.example.carrental.controller.dto.user;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display User Driving License data.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDrivingLicenseDataResponse {

  @NotNull
  @Past
  private LocalDate dateOfIssue;
  @NotNull
  private LocalDate validityPeriod;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String organizationThatIssued;
}
