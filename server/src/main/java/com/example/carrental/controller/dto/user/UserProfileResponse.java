package com.example.carrental.controller.dto.user;

import com.example.carrental.entity.user.UserDocumentStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

  //TODO: add validation!
  private long id;
  private String email;
  private List<String> phones;
  private boolean isEmailConfirmed;
  private String passportStatus;
  private String drivingLicenseStatus;
}
