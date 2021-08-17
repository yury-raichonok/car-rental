package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display User Driving License confirmation data.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDrivingLicenseConfirmationDataResponse {

  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String dateOfIssue;
  @NotNull
  @Size(
      min = 1,
      max = 20
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
