package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for confirmation User Phone number.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneConfirmationRequest {

  @Pattern(
      regexp = "^[0-9]{4}$"
  )
  private String token;
  @NotNull
  private String phoneNumber;
}
