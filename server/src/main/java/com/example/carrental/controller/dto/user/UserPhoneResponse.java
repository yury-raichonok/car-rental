package com.example.carrental.controller.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  private String phone;
  @NotNull
  private boolean active;
}
