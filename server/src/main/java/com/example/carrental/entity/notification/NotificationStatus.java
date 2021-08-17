package com.example.carrental.entity.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents Notification statuses.
 *
 * @author Yury Raichonak
 */
@Getter
@AllArgsConstructor
public enum NotificationStatus {

  NEW("New"),
  READED("Readed"),
  DELETED("Deleted");

  private final String status;
}
