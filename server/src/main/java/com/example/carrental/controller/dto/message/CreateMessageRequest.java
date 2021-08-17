package com.example.carrental.controller.dto.message;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for creating Messages.
 *
 * @author Yury Raichonak
 */
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
  @NotNull
  @Email
  @Size(
      min = 1,
      max = 255
  )
  private String email;
  @NotNull
  private String phone;
  @NotNull
  @Size(
      min = 1,
      max = 2024
  )
  private String message;
}
