package com.example.carrental.controller.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for updating User.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

  @Email
  private String email;
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
  )
  private String password;
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
  )
  private String confirmPassword;
}
