package com.example.carrental.controller.dto.user;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneConfirmationRequest {

  @Pattern(
      regexp = "^[0-9]{4}$"
  )
  private String token;
  private String phoneNumber;
}
