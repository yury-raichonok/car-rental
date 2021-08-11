package com.example.carrental.controller.dto.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDataResponse {

  private String email;
  private boolean emailConfirmed;
  private boolean passportConfirmed;
  private boolean drivingLicenseConfirmed;
  private List<UserPhoneResponse> phones;
}
