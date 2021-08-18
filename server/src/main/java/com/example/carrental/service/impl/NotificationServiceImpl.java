package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import com.example.carrental.mapper.NotificationMapper;
import com.example.carrental.repository.NotificationRepository;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.UserSecurityService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for Notifications.
 * <p>
 * This class performs the CRUD operations for Notifications.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationMapper notificationMapper;
  private final NotificationRepository notificationRepository;
  private final UserSecurityService userSecurityService;

  /**
   * @param pageable data.
   * @return page of notification response.
   */
  @Override
  public Page<NotificationResponse> findAllNew(Pageable pageable) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var notifications = notificationRepository
        .findAllByUser_EmailAndStatus(userEmail, NotificationStatus.NEW, pageable);
    List<NotificationResponse> notificationsResponse = new ArrayList<>();
    notifications.forEach(notification -> notificationsResponse
        .add(notificationMapper.notificationToNotificationResponse(notification)));
    return new PageImpl<>(notificationsResponse, notifications.getPageable(),
        notifications.getTotalElements());
  }

  /**
   * @param pageable data.
   * @return page of notification history response.
   */
  @Override
  public Page<NotificationResponse> findAllNotificationsHistory(Pageable pageable) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var notifications = notificationRepository
        .findAllByUser_EmailAndStatus(userEmail, NotificationStatus.READED, pageable);
    List<NotificationResponse> notificationsResponse = new ArrayList<>();
    notifications.forEach(notification -> notificationsResponse
        .add(notificationMapper.notificationToNotificationResponse(notification)));
    return new PageImpl<>(notificationsResponse, notifications.getPageable(),
        notifications.getTotalElements());
  }

  /**
   * @param notification data for creating new notification.
   */
  @Override
  public void sendNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  /**
   * @param id of updated notification.
   */
  @Override
  @Transactional
  public void updateNotificationAsRead(Long id) {
    var notification = findById(id);
    notification.setStatus(NotificationStatus.READED);
    notificationRepository.save(notification);
  }

  /**
   * @param id of notification.
   * @return notification.
   */
  @Override
  public Notification findById(Long id) {
    return notificationRepository.findById(id).orElseThrow(() -> {
      log.error("Notification with id {} does not exist", id);
      throw new IllegalStateException(String.format("Notification with id %d does not exists", id));
    });
  }

  /**
   * @param id of notification.
   */
  @Override
  @Transactional
  public void delete(Long id) {
    var notification = findById(id);
    notification.setStatus(NotificationStatus.DELETED);
    notificationRepository.save(notification);
  }

  /**
   * @return amount of new user notifications.
   */
  @Override
  public int findNewUserNotificationsAmount() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    return notificationRepository.countAllByUser_EmailAndStatus(userEmail, NotificationStatus.NEW);
  }
}
