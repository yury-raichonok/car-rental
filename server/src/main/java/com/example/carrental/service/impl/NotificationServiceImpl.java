package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import com.example.carrental.entity.user.User;
import com.example.carrental.mapper.NotificationMapper;
import com.example.carrental.repository.NotificationRepository;
import com.example.carrental.service.NotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @Transactional
  public void sendNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  @Override
  public Page<NotificationResponse> findAllNew(Pageable pageable) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var notifications = notificationRepository
        .findAllByUser_EmailAndStatus(user.getEmail(), NotificationStatus.NEW, pageable);
    List<NotificationResponse> responses = new ArrayList<>();
    notifications.forEach(notification -> responses
        .add(notificationMapper.notificationToNotificationResponse(notification)));

    return new PageImpl<>(responses, notifications.getPageable(), notifications.getTotalElements());
  }

  @Override
  public Page<NotificationResponse> findAllNotificationsHistory(Pageable pageable) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }
    var notifications = notificationRepository
        .findAllByUser_EmailAndStatus(user.getEmail(), NotificationStatus.READED, pageable);
    List<NotificationResponse> responses = new ArrayList<>();
    notifications.forEach(notification -> responses
        .add(notificationMapper.notificationToNotificationResponse(notification)));

    return new PageImpl<>(responses, notifications.getPageable(), notifications.getTotalElements());
  }

  @Override
  @Transactional
  public String updateNotificationAsRead(Long id) {
    var notification = findById(id);
    notification.setStatus(NotificationStatus.READED);
    notificationRepository.save(notification);
    return "Success";
  }

  @Override
  public Notification findById(Long id) {
    var optionalNotification = notificationRepository.findById(id);
    if (optionalNotification.isEmpty()) {
      log.error("Notification with id {} does not exist", id);
      throw new IllegalStateException(String.format("Notification with id %d does not exists", id));
    }
    return optionalNotification.get();
  }

  @Override
  @Transactional
  public String delete(Long id) {
    var notification = findById(id);
    notification.setStatus(NotificationStatus.DELETED);
    notificationRepository.save(notification);
    return "Success";
  }

  @Override
  public int findNewUserNotificationsAmount(String email) {
    return notificationRepository.countAllByUser_EmailAndStatus(email, NotificationStatus.NEW);
  }
}
