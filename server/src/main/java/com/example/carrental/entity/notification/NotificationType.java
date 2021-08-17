package com.example.carrental.entity.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents Notification types.
 *
 * @author Yury Raichonak
 */
@Getter
@AllArgsConstructor
public enum NotificationType {

  ACCEPT("Accept"),
  DENY("Deny"),
  INFO("Info");

  private final String type;
}
