package com.example.carrental.service;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

  void sendNotification(Notification notification);

  Page<NotificationResponse> findAllNew(Pageable pageable);

  Page<NotificationResponse> findAllNotificationsHistory(Pageable pageable);

  String updateNotificationAsRead(Long id);

  Notification findById(Long id);

  String delete(Long id);

  int findNewUserNotificationsAmount(String email);
}
