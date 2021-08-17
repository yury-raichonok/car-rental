package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object sending User Phone confirmation SMS.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSmsRequest {

  @NotNull
  private String phoneNumber;
}
