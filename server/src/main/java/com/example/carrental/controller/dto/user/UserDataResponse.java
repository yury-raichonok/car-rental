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
 * Data transfer object for display User data.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponse {

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
  @NotNull
  @Size(
      min = 1,
      max = 5
  )
  private String role;
  @NotNull
  private boolean locked;
  @NotNull
  private boolean emailConfirmed;
  @NotNull
  private String passportStatus;
  @NotNull
  private String drivingLicenseStatus;
  private List<UserPhoneResponse> phones;
  private Long passportId;
  private Long drivingLicenseId;
}
