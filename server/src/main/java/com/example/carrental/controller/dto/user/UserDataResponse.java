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
public class UserDataResponse {

  private Long id;
  private String email;
  private String role;
  private boolean locked;
  private boolean emailConfirmed;
  private String passportStatus;
  private String drivingLicenseStatus;
  private List<UserPhoneResponse> phones;
  private Long passportId;
  private Long drivingLicenseId;
}
