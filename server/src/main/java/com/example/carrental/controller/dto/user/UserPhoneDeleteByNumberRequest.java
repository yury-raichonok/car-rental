package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for changing User Phone status.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneDeleteByNumberRequest {

  @NotNull
  private String phone;
}
