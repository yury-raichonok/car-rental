package com.example.carrental.service.impl;

import static com.example.carrental.entity.notification.NotificationStatus.READED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import com.example.carrental.repository.NotificationRepository;
import com.example.carrental.service.UserSecurityService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationServiceImplTest {

  @Autowired
  private NotificationServiceImpl notificationService;

  @MockBean
  private NotificationRepository notificationRepository;

  @MockBean
  private UserSecurityService userSecurityService;

  @Test
  void givenValidRequest_whenFindAllNew_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Arrays.asList(Notification.builder().id(1L).build(),
        Notification.builder().id(2L).build()));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(notificationRepository.findAllByUser_EmailAndStatus(userEmail, NotificationStatus.NEW,
        pageable)).thenReturn(response);

    var notificationResponse = notificationService.findAllNew(pageable);

    assertThat(notificationResponse).isNotNull();
    assertThat(notificationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNotificationsHistory_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Arrays.asList(Notification.builder().id(1L).build(),
        Notification.builder().id(2L).build()));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(notificationRepository.findAllByUser_EmailAndStatus(userEmail, READED,
        pageable)).thenReturn(response);

    var notificationResponse = notificationService.findAllNotificationsHistory(pageable);

    assertThat(notificationResponse).isNotNull();
    assertThat(notificationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenSendNotification_thenSuccess() {
    var notification = Notification.builder().id(1L).build();
    when(notificationRepository.save(notification)).thenReturn(new Notification());

    assertDoesNotThrow(() -> notificationService.sendNotification(notification));
  }

  @Test
  void givenValidRequest_whenUpdateNotificationAsRead_thenSuccess() {
    var notification = Optional
        .of(Notification.builder().id(1L).status(NotificationStatus.NEW).build());
    when(notificationRepository.findById(1L)).thenReturn(notification);

    notificationService.updateNotificationAsRead(1L);

    assertEquals(READED, notification.get().getStatus());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var optionalNotification = Optional
        .of(Notification.builder().id(1L).status(NotificationStatus.NEW).build());
    when(notificationRepository.findById(1L)).thenReturn(optionalNotification);

    var notification = notificationService.findById(1L);

    assertThat(notification).isNotNull();
    assertThat(notification.getId()).isEqualTo(optionalNotification.get().getId());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> notificationService.findById(1L));
  }

  @Test
  void givenValidRequest_whenDelete_thenSuccess() {
    var notification = Optional.of(Notification.builder().id(1L).build());
    when(notificationRepository.findById(1L)).thenReturn(notification);
    doNothing().when(notificationRepository).delete(any(Notification.class));

    assertDoesNotThrow(() -> notificationService.delete(1L));
  }

  @Test
  void givenValidRequest_whenFindNewUserNotificationsAmount_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(notificationRepository.countAllByUser_EmailAndStatus(userEmail, NotificationStatus.NEW))
        .thenReturn(8);

    var expectedAmount = 8;
    var amount = notificationService.findNewUserNotificationsAmount();

    assertEquals(expectedAmount, amount);
  }
}