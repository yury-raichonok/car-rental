package com.example.carrental.mapper;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  String NOTIFICATION_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

  @Mapping(target = "id", source = "notification.id")
  @Mapping(target = "message", source = "notification.message")
  @Mapping(target = "notificationType", source = "notification.notificationType.notificationType")
  @Mapping(target = "sentDate", source = "notification.sentDate",
      dateFormat = NOTIFICATION_DATE_FORMAT_PATTERN)
  NotificationResponse notificationToNotificationResponse(Notification notification);
}
