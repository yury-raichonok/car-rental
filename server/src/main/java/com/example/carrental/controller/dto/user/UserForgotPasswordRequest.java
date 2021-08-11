package com.example.carrental.controller.dto.user;

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
public class UserForgotPasswordRequest {

  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
}
