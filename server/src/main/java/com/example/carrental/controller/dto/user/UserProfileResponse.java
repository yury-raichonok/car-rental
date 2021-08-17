package com.example.carrental.controller.dto.user;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for display User profile.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  private List<String> phones;
  @NotNull
  private boolean isEmailConfirmed;
  @NotNull
  private String passportStatus;
  @NotNull
  private String drivingLicenseStatus;
}
