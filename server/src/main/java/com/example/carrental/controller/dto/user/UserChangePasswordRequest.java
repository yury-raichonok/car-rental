package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for changing User password.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

  @NotBlank
  private String token;
    @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
  )
  private String password;
    @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
  )
  private String confirmPassword;
}
