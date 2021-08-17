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

/**
 * Data transfer object for creating or updating User Driving License.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateUserDrivingLicenseRequest {

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
