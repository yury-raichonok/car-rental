package com.example.carrental.controller.dto.notification;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  @NotNull
  @Positive
  private long id;
  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String message;
  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String notificationType;
  @NotNull
  @PastOrPresent
  private String sentDate;
}
