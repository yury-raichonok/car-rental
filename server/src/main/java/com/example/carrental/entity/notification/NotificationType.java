package com.example.carrental.entity.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

  ACCEPT("Accept"),
  DENY("Deny"),
  INFO("Info");

  private final String notificationType;
}
