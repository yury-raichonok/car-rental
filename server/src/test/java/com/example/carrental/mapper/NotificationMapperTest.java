package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationMapperTest {

  @Autowired
  private NotificationMapper notificationMapper;

  @Test
  void notificationToNotificationResponse() {
    var sentDate = LocalDateTime.now();
    var notification = Notification.builder().id(1L).message("message").notificationType(
        NotificationType.ACCEPT).sentDate(sentDate).build();

    var notificationResponse = notificationMapper.notificationToNotificationResponse(notification);

    assertThat(notificationResponse).isNotNull();
    assertThat(notificationResponse.getId()).isEqualTo(notification.getId());
    assertThat(notificationResponse.getMessage()).isEqualTo(notification.getMessage());
    assertThat(notificationResponse.getSentDate()).isEqualTo(notification.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }
}