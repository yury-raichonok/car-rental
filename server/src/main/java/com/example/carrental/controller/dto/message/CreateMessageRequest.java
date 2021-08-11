package com.example.carrental.controller.dto.message;

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
public class CreateMessageRequest {

  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String name;
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  @NotNull
//  @Pattern(regexp = "^+\\d{14}$")
  private String phone;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String message;
}
