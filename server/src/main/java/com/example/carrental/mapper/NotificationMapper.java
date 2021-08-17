package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "id", source = "notification.id")
  @Mapping(target = "message", source = "notification.message")
  @Mapping(target = "notificationType", source = "notification.notificationType.type")
  @Mapping(target = "sentDate", source = "notification.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  NotificationResponse notificationToNotificationResponse(Notification notification);
}
