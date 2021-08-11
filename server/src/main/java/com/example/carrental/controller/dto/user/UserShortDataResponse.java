package com.example.carrental.controller.dto.user;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDataResponse {

  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  @NotNull
  private boolean emailConfirmed;
  @NotNull
  private boolean passportConfirmed;
  @NotNull
  private boolean drivingLicenseConfirmed;
  private List<UserPhoneResponse> phones;
}
